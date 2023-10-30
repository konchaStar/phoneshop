package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.dto.OrderDto;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderPageController {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ADDRESS = "deliveryAddress";
    private static final String PHONE = "contactPhoneNo";
    private static final String ERRORS_ATTRIBUTE = "errors";
    @Resource
    private CartService cartService;
    @Resource
    private OrderService orderService;
    @Resource
    private StockDao stockDao;

    @GetMapping
    public String getOrder(Model model) throws OutOfStockException {
        Order order = orderService.createOrder(cartService.getCart());
        OrderDto orderDto = OrderDto.createOrderDto(order);
        model.addAttribute("order", orderDto);
        return "order";
    }

    @PostMapping
    public String placeOrder(@ModelAttribute("order") @Valid OrderDto orderDto, BindingResult br, Model model) {
        Map<String, String> validationErrors = new HashMap<>();
        List<String> outOfStockErrors = new ArrayList<>();
        Cart cart = cartService.getCart();
        handleErrors(model, validationErrors, outOfStockErrors, br, cart);
        Order cartOrder = orderService.createOrder(cart);
        orderDto.setOrderItems(cartOrder.getOrderItems());
        if (!(outOfStockErrors.isEmpty() && validationErrors.isEmpty())) {
            model.addAttribute("outOfStockErrors", outOfStockErrors);
            return "orderDto";
        }
        if (orderDto.getOrderItems().isEmpty()) {
            return "redirect:productList";
        }
        cartService.clear();
        Order order = orderService.createOrderByDto(orderDto);
        orderService.placeOrder(order);
        return "redirect:orderOverview/" + order.getSecureId();
    }

    private void handleErrors(Model model, Map<String, String> validationErrors,
                              List<String> outOfStockErrors, BindingResult br, Cart cart) {
        if (br.hasErrors()) {
            if (br.hasFieldErrors(FIRST_NAME)) {
                validationErrors.put(FIRST_NAME, br.getFieldError(FIRST_NAME).getDefaultMessage());
            }
            if (br.hasFieldErrors(LAST_NAME)) {
                validationErrors.put(LAST_NAME, br.getFieldError(LAST_NAME).getDefaultMessage());
            }
            if (br.hasFieldErrors(ADDRESS)) {
                validationErrors.put(ADDRESS, br.getFieldError(ADDRESS).getDefaultMessage());
            }
            if (br.hasFieldErrors(PHONE)) {
                validationErrors.put(PHONE, br.getFieldError(PHONE).getDefaultMessage());
            }
            model.addAttribute(ERRORS_ATTRIBUTE, validationErrors);
        }
        List<Phone> outOfStockPhones = cart.getPhones().entrySet().stream()
                .filter(entry -> {
                    Stock stock = stockDao.getAvailableStock(entry.getKey().getId());
                    return stock.getStock() - stock.getReserved() - entry.getValue() < 0;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        outOfStockPhones.forEach(phone -> {
                    cartService.remove(phone.getId());
                    outOfStockErrors.add(String.format("Phone %s is out of stock", phone.getModel()));
                });
    }
}
