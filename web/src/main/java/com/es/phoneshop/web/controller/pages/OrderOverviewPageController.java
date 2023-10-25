package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    private static String ORDER_ATTRIBUTE = "order";
    @Resource
    private OrderService orderService;

    @GetMapping(path = "/{secureId}")
    public String getOrderOverview(@PathVariable String secureId, Model model) {
        Order order = orderService.getOrderBySecureId(secureId);
        model.addAttribute(ORDER_ATTRIBUTE, order);
        return "orderOverview";
    }
}
