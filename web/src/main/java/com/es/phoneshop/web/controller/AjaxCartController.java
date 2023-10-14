package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.dto.CartItemDto;
import com.es.core.dto.QuantityAddToCartDto;
import com.es.core.dto.QuantityCartItemDto;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Color;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public QuantityAddToCartDto addPhone(@RequestBody @Valid QuantityCartItemDto cartDto,
                                         BindingResult bindingResult) {
        QuantityAddToCartDto message = new QuantityAddToCartDto();
        if (!bindingResult.hasErrors()) {
            message.setMessage("Phone was successfully added");
            message.setErrorStatus(false);
            cartService.addPhone(cartDto.getPhoneId(), cartDto.getQuantity());
        } else {
            message.setErrorStatus(true);
            message.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        message.setPhoneId(cartDto.getPhoneId());
        message.setTotalQuantity(cartService.getCart().getTotalQuantity());
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public CartItemDto deleteCartItem(@RequestBody Long id) {
        cartService.remove(id);
        return getCartItemDto();
    }
    private CartItemDto getCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        List<String> models = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> phone.getModel())
                .collect(Collectors.toList());
        List<String> brands = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> phone.getBrand())
                .collect(Collectors.toList());
        List<Set<Color>> colors = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> phone.getColors())
                .collect(Collectors.toList());
        List<BigDecimal> displaySizesInches = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> phone.getDisplaySizeInches())
                .collect(Collectors.toList());
        List<BigDecimal> prices = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> phone.getPrice())
                .collect(Collectors.toList());
        List<Long> quantities = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> cartService.getCart().getPhones().get(phone))
                .collect(Collectors.toList());
        List<Long> ids = cartService.getCart().getPhones().keySet()
                .stream()
                .map(phone -> phone.getId())
                .collect(Collectors.toList());
        Long totalQuantity = cartService.getCart().getTotalQuantity();
        cartItemDto.setBrands(brands);
        cartItemDto.setModels(models);
        cartItemDto.setColors(colors);
        cartItemDto.setPrices(prices);
        cartItemDto.setQuantities(quantities);
        cartItemDto.setDisplaySizesInches(displaySizesInches);
        cartItemDto.setTotalQuantity(totalQuantity);
        cartItemDto.setIds(ids);
        return cartItemDto;
    }
    @ExceptionHandler(InvalidFormatException.class)
    public QuantityAddToCartDto InvalidFormatException(InvalidFormatException e) {
        QuantityAddToCartDto message = new QuantityAddToCartDto();
        message.setMessage("Quantity must be number");
        message.setErrorStatus(true);
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
