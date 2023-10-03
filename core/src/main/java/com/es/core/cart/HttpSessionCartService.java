package com.es.core.cart;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    @Autowired
    private ObjectFactory<Cart> cartObjectFactory;
    public Cart getCart() {
        return cartObjectFactory.getObject();
    }

    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

    public HttpSessionCartService() {
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Optional<Phone> phone = phoneDao.get(phoneId);
        Cart cart = getCart();
        Stock stock = stockDao.getAvailableStock(phoneId);
        if (phone.isPresent() && stock.getStock() - stock.getReserved() - quantity > 0) {
            if (cart.getPhones().keySet().contains(phone.get())) {
                Long cartQuantity = cart.getPhones().get(phone.get());
                cart.getPhones().replace(phone.get(), cartQuantity + quantity);
            } else {
                cart.getPhones().put(phone.get(), quantity);
            }
            recalculate();
        } else {
            throw new OutOfStockException("Out of stock. Max quantity " + (stock.getStock() - stock.getReserved()));
        }
    }

    @Override
    public void update(Map<Long, Long> items) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void remove(Long phoneId) {
        throw new UnsupportedOperationException("TODO");
    }

    private void recalculate() {
        Cart cart = getCart();
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Phone phone : cart.getPhones().keySet()) {
            totalQuantity = totalQuantity.add(BigDecimal.valueOf(cart.getPhones().get(phone)));
            totalPrice = totalPrice.add(phone.getPrice() == null ? BigDecimal.ZERO : phone.getPrice().multiply(
                    BigDecimal.valueOf(cart.getPhones().get(phone))));
        }
        cart.setTotalQuantity(totalQuantity.longValue());
        cart.setTotalPrice(totalPrice);
    }
}
