package com.es.phoneshop.web.controller.pages;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginPageController {
    @GetMapping
    public String showLoginPage() {
        return "login";
    }
    @GetMapping
    @RequestMapping("/postLogin")
    public String postLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated()) {
            return "redirect:/productList";
        }
        return "login";
    }

}
