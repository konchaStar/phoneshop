package com.es.core.model.order;

import com.es.core.model.phone.Phone;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Phone phone;
    private Long quantity;

    public OrderItem(Phone phone, Long quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }
}
