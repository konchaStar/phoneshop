package com.es.core.cart;

import com.es.core.exception.OutOfStockException;
import com.es.core.exception.ProductNotFoundException;
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
    public void addPhone(Long phoneId, Integer quantity) {
        Optional<Phone> optionalPhone = phoneDao.get(phoneId);
        Cart cart = getCart();
        if (optionalPhone.isPresent()) {
            Phone phone = optionalPhone.get();
            Stock stock = stockDao.getAvailableStock(phoneId);
            Integer cartQuantity = Optional.ofNullable(cart.getPhones().get(phone)).orElse(0);
            if (stock.getStock() - stock.getReserved() - quantity - cartQuantity >= 0) {
                if (cart.getPhones().containsKey(phone)) {
                    cart.getPhones().replace(phone, cartQuantity + quantity);
                } else {
                    cart.getPhones().put(phone, quantity);
                }
                recalculate();
            } else {
                throw new OutOfStockException("Out of stock. Max quantity " + (stock.getStock() - stock.getReserved()));
            }
        }
    }

    @Override
    public void update(Map<Long, Integer> items) {
        Cart cart = getCart();
        items.forEach((key, value) -> {
            Optional<Phone> optionalPhone = phoneDao.get(key);
            if (optionalPhone.isPresent()) {
                cart.getPhones().replace(optionalPhone.get(), value);
            } else {
                Phone phone = findPhoneById(key);
                cart.getPhones().remove(phone);
            }
        });
        recalculate();
    }
    private Phone findPhoneById(Long id) {
        return getCart().getPhones().keySet().stream()
                .filter(phone -> phone.getId().equals(id))
                .findFirst().orElseThrow(() -> new ProductNotFoundException("Phone wasn't found"));
    }
    @Override
    public void remove(Long phoneId) {
        Cart cart = getCart();
        phoneDao.get(phoneId).ifPresent(phone -> cart.getPhones().remove(phone));
        recalculate();
    }

    private void recalculate() {
        Cart cart = getCart();
        Integer totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<Phone, Integer> entry : cart.getPhones().entrySet()) {
            totalQuantity += entry.getValue();
            totalPrice = totalPrice.add(Optional.ofNullable(entry.getKey().getPrice()).orElse(BigDecimal.ZERO)
                    .multiply(BigDecimal.valueOf(entry.getValue())));
        }
        cart.setTotalQuantity(totalQuantity);
        cart.setTotalPrice(totalPrice);
    }

    @Override
    public void clear() {
        Cart cart = getCart();
        cart.getPhones().clear();
        recalculate();
    }
}
