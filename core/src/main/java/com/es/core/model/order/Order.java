package com.es.core.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private List<OrderItem> orderItems;
    /**
     * A sum of order item prices;
     */
    private BigDecimal subtotal;
    private BigDecimal deliveryPrice;
    private String secureId;
    /**
     * <code>subtotal</code> + <code>deliveryPrice</code>
     */
    private BigDecimal totalPrice;
    private String firstName;
    private String lastName;
    private String deliveryAddress;
    private String contactPhoneNo;

    private OrderStatus status;

}
