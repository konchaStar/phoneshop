package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.dto.QuantityAddToCartDto;
import com.es.core.dto.QuantityCartItemDto;
import com.es.core.exception.OutOfStockException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public QuantityAddToCartDto addPhone(@RequestBody @Valid QuantityCartItemDto cartDto,
                                         BindingResult bindingResult) {
        QuantityAddToCartDto message = new QuantityAddToCartDto();
        if(!bindingResult.hasErrors()){
            message.setMessage("Phone was successfully added");
            message.setErrorStatus(false);
            cartService.addPhone(cartDto.getPhoneId(), Long.parseLong(cartDto.getQuantity()));
        } else {
            message.setErrorStatus(true);
            message.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        message.setPhoneId(cartDto.getPhoneId());
        message.setTotalQuantity(cartService.getCart().getTotalQuantity());
        return message;
    }

    @ExceptionHandler(OutOfStockException.class)
    public QuantityAddToCartDto OutOfStockException(OutOfStockException e) {
        QuantityAddToCartDto message = new QuantityAddToCartDto();
        message.setMessage(e.getMessage());
        message.setErrorStatus(true);
        message.setTotalQuantity(cartService.getCart().getTotalQuantity());
        return message;
    }
}
