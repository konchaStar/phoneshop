package com.es.core.cart;

import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Integer quantity);

    void update(Map<Long, Integer> items);

    void remove(Long phoneId);
    void clear();
}
