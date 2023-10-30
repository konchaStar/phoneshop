package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.dto.OrderDto;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class OrdersPageController {
    private static final String ORDERS_ATTRIBUTE = "orders";
    private static final String ORDER_ATTRIBUTE = "order";
    @Resource
    private OrderService orderService;

    @GetMapping
    public String showOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute(ORDERS_ATTRIBUTE, orders);
        return "ordersPage";
    }
    @GetMapping("/{id}")
    public String showOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        OrderDto orderDto = OrderDto.createOrderDto(order);
        model.addAttribute(ORDER_ATTRIBUTE, orderDto);
        return "adminOrderOverview";
    }
    @PostMapping("changeStatus/{id}")
    public String updateOrderStatus(@PathVariable Long id, OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }
}
