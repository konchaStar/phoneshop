package com.es.core.cart;

import com.es.core.model.phone.Phone;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(scopeName = "session")
@Data
public class Cart {
    private Map<Phone, Integer> phones;
    private Integer totalQuantity;
    private BigDecimal totalPrice;

    public Cart() {
        phones = new HashMap<>();
        totalPrice = BigDecimal.ZERO;
        totalQuantity = 0;
    }
}
