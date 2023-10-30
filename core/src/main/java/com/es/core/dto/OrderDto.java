package com.es.core.dto;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private List<OrderItem> orderItems;
    private BigDecimal subtotal;
    private BigDecimal deliveryPrice;
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

    public static OrderDto createOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderItems(order.getOrderItems());
        orderDto.setStatus(order.getStatus());
        orderDto.setFirstName(order.getFirstName());
        orderDto.setDeliveryPrice(order.getDeliveryPrice());
        orderDto.setSubtotal(order.getSubtotal());
        orderDto.setContactPhoneNo(order.getContactPhoneNo());
        orderDto.setLastName(order.getLastName());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setDeliveryAddress(order.getDeliveryAddress());
        return orderDto;
    }
}