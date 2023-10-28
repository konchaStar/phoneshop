package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.dto.CartCostQuantityDto;
import com.es.core.dto.AddToCartResponseDto;
import com.es.core.dto.QuantityPhoneIdDto;
import com.es.core.exception.OutOfStockException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @PostMapping(produces = "application/json")
    public AddToCartResponseDto addPhone(@RequestBody @Valid QuantityPhoneIdDto quantityPhoneIdDto,
                                         BindingResult bindingResult) {
        AddToCartResponseDto message = new AddToCartResponseDto();
        if (!bindingResult.hasErrors()) {
            message.setMessage("Phone was successfully added");
            message.setErrorStatus(false);
            cartService.addPhone(quantityPhoneIdDto.getPhoneId(), quantityPhoneIdDto.getQuantity());
        } else {
            message.setErrorStatus(true);
            message.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        message.setPhoneId(quantityPhoneIdDto.getPhoneId());
        message.setTotalQuantity(cartService.getCart().getTotalQuantity());
        message.setTotalPrice(cartService.getCart().getTotalPrice());
        return message;
    }

    @DeleteMapping(produces = "application/json")
    public CartCostQuantityDto deleteCartItem(@RequestBody Long id) {
        cartService.remove(id);
        return new CartCostQuantityDto(cartService.getCart().getTotalQuantity(),
                cartService.getCart().getTotalPrice());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public AddToCartResponseDto InvalidFormatException(InvalidFormatException e) {
        AddToCartResponseDto message = new AddToCartResponseDto();
        message.setMessage("Quantity must be number");
        message.setErrorStatus(true);
        message.setTotalQuantity(cartService.getCart().getTotalQuantity());
        message.setTotalPrice(cartService.getCart().getTotalPrice());
        return message;
    }

    @ExceptionHandler(OutOfStockException.class)
    public AddToCartResponseDto OutOfStockException(OutOfStockException e) {
        AddToCartResponseDto message = new AddToCartResponseDto();
        message.setMessage(e.getMessage());
        message.setErrorStatus(true);
        message.setTotalQuantity(cartService.getCart().getTotalQuantity());
        message.setTotalPrice(cartService.getCart().getTotalPrice());
        return message;
    }
}
