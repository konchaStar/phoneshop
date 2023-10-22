package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String ADDRESS = "deliveryAddress";
    private static String PHONE = "contactPhoneNo";
    private static String ERRORS_ATTRIBUTE = "errors";
    @Resource
    private CartService cartService;
    @Resource
    private OrderService orderService;
    @Resource
    private StockDao stockDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) throws OutOfStockException {
        Order order = orderService.createOrder(cartService.getCart());
        model.addAttribute("order", order);
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@ModelAttribute("order") @Valid Order order, BindingResult br, Model model) {
        Map<String, String> validationErrors = new HashMap<>();
        if(br.hasErrors()) {
            if(br.hasFieldErrors(FIRST_NAME)) {
                validationErrors.put(FIRST_NAME, br.getFieldError(FIRST_NAME).getDefaultMessage());
            }
            if(br.hasFieldErrors(LAST_NAME)) {
                validationErrors.put(LAST_NAME, br.getFieldError(LAST_NAME).getDefaultMessage());
            }
            if(br.hasFieldErrors(ADDRESS)) {
                validationErrors.put(ADDRESS, br.getFieldError(ADDRESS).getDefaultMessage());
            }
            if(br.hasFieldErrors(PHONE)) {
                validationErrors.put(PHONE, br.getFieldError(PHONE).getDefaultMessage());
            }
            model.addAttribute(ERRORS_ATTRIBUTE, validationErrors);
        }
        List<String> outOfStockErrors = new ArrayList<>();
        Cart cart = cartService.getCart();
        List<Phone> outOfStockPhones = cart.getPhones().keySet().stream()
                .filter(phone -> {
                    Stock stock = stockDao.getAvailableStock(phone.getId());
                    return stock.getStock() - stock.getReserved() - cart.getPhones().get(phone) < 0;
                })
                .collect(Collectors.toList());
        outOfStockPhones.stream()
                .forEach(phone -> {
                    cartService.remove(phone.getId());
                    outOfStockErrors.add(String.format("Phone %s is out of stock", phone.getModel()));
                });
        Order cartOrder = orderService.createOrder(cart);
        order.setOrderItems(cartOrder.getOrderItems());
        if(!(outOfStockErrors.isEmpty() && validationErrors.isEmpty())) {
            model.addAttribute("outOfStockErrors", outOfStockErrors);
            return "order";
        }
        cartService.clear();
        orderService.placeOrder(order);
        return "redirect:orderOverview/" + order.getSecureId();
    }
}
