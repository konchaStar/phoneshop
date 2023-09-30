package com.es.core.cart;

import com.es.core.model.phone.Phone;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Cart {
    private Map<Phone, Long> phones;
    private Long totalQuantity;
    private BigDecimal totalPrice;
    public Cart() {
        phones = new HashMap<>();
        totalPrice = BigDecimal.ZERO;
        totalQuantity = 0L;
    }
}
