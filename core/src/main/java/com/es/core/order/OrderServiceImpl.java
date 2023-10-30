package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.dto.OrderDto;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;
    @Resource
    private OrderDao orderDao;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setDeliveryPrice(deliveryPrice);
        order.setOrderItems(cart.getPhones().entrySet().stream()
                .map(entry -> new OrderItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalPrice());
        order.setTotalPrice(order.getDeliveryPrice().add(order.getSubtotal()));
        return order;
    }

    @Override
    public Order createOrderByDto(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderItems(orderDto.getOrderItems());
        order.setStatus(orderDto.getStatus());
        order.setDeliveryPrice(orderDto.getDeliveryPrice());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setSubtotal(orderDto.getSubtotal());
        order.setFirstName(orderDto.getFirstName());
        order.setLastName(orderDto.getLastName());
        order.setContactPhoneNo(orderDto.getContactPhoneNo());
        order.setDeliveryAddress(order.getDeliveryAddress());
        return order;
    }

    @Transactional
    @Override
    public void placeOrder(Order order) {
        orderDao.placeOrder(order);
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        return orderDao.getOrderBySecureId(secureId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderDao.getOrderById(id);
    }

    @Override
    public List<Order> getOrders() {
        return orderDao.getOrders();
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        orderDao.updateOrderStatus(id, status);
    }
}
