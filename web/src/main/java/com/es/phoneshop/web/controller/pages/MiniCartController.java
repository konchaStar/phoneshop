package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping("/cart/minicart")
public class MiniCartController {
    @Resource
    private CartService cartService;
    @RequestMapping(method = RequestMethod.GET)
    public String showMiniCart(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "minicart";
    }
    @RequestMapping(method = RequestMethod.POST)
    public String showMiniCartPost(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "minicart";
    }
}