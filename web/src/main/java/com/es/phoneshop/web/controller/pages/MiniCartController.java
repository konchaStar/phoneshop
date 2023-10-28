package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/cart/minicart")
public class MiniCartController {
    @Resource
    private CartService cartService;
    @GetMapping
    public String showMiniCart(Model model) {
        return addMiniCartAttribute(model);
    }
    @PostMapping
    public String showMiniCartPost(Model model) {
        return addMiniCartAttribute(model);
    }
    private String addMiniCartAttribute(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "minicart";
    }
}