package com.es.core.cart;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private ObjectFactory<Cart> cartObjectFactory;

    public Cart getCart() {
        return cartObjectFactory.getObject();
    }

    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Optional<Phone> phone = phoneDao.get(phoneId);
        Cart cart = getCart();
        if (phone.isPresent()) {
            Stock stock = stockDao.getAvailableStock(phoneId);
            Long cartQuantity = Optional.ofNullable(cart.getPhones().get(phone.get())).orElse(0l);
            if (stock.getStock() - stock.getReserved() - quantity - cartQuantity >= 0) {
                if (cart.getPhones().containsKey(phone.get())) {
                    cart.getPhones().replace(phone.get(), cartQuantity + quantity);
                } else {
                    cart.getPhones().put(phone.get(), quantity);
                }
                recalculate();
            } else {
                throw new OutOfStockException("Out of stock. Max quantity " + (stock.getStock() - stock.getReserved()));
            }
        }
    }

    @Override
    public void update(Map<Long, Long> items) {
        Cart cart = getCart();
        for (Long phoneId : items.keySet()) {
            Stock stock = stockDao.getAvailableStock(phoneId);
            if(stock.getStock() - items.get(phoneId) - stock.getReserved() < 0) {
                throw new OutOfStockException("Out of stock. Max quantity " + (stock.getStock() - stock.getReserved()), phoneId);
            }
        }
        cart.getPhones().clear();
        items.keySet().stream()
                .map(id -> Map.of(phoneDao.get(id).get(), items.get(id)))
                .forEach(cart.getPhones()::putAll);
        recalculate();
    }

    @Override
    public void remove(Long phoneId) {
        Cart cart = getCart();
        Phone phone = phoneDao.get(phoneId).get();
        cart.getPhones().remove(phone);
        recalculate();
    }

    private void recalculate() {
        Cart cart = getCart();
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Phone phone : cart.getPhones().keySet()) {
            Long quantity = cart.getPhones().get(phone);
            totalQuantity = totalQuantity.add(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(phone.getPrice() == null ? BigDecimal.ZERO : phone.getPrice().multiply(
                    BigDecimal.valueOf(quantity)));
        }
        cart.setTotalQuantity(totalQuantity.longValue());
        cart.setTotalPrice(totalPrice);
    }
}
