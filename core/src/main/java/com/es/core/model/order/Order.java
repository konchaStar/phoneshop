package com.es.core.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@Component
@Scope(scopeName = "prototype")
@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private List<OrderItem> orderItems;
    /**
     * A sum of order item prices;
     */
    private BigDecimal subtotal;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;
    private String secureId;
    /**
     * <code>subtotal</code> + <code>deliveryPrice</code>
     */
    private BigDecimal totalPrice;
    @NotBlank(message = "Name is required")
    private String firstName;
    @NotBlank(message = "Surname is required")
    private String lastName;
    @NotBlank(message = "Address is required")
    private String deliveryAddress;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\+375(33|29|25|44)\\d{7}", message = "Invalid phone number")
    private String contactPhoneNo;

    private OrderStatus status;

}
