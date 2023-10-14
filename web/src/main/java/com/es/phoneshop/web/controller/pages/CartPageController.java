package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.dto.CartItemsUpdateDto;
import com.es.core.exception.OutOfStockException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String INVALID_FORMAT_DEFAULT_MESSAGE = "Failed to convert property value";
    private static final String INVALID_FORMAT_MESSAGE = "Quantity must be number";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String ITEMS_DTO_ATTRIBUTE = "itemsDto";
    private static final String VALID_ERRORS_ATTRIBUTE = "validationErrors";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String ERROR_FIELD = "items[%d].quantity";
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        model.addAttribute(CART_ATTRIBUTE, cartService.getCart());
        CartItemsUpdateDto itemsDto = new CartItemsUpdateDto();
        itemsDto.copyFromCart(cartService.getCart());
        model.addAttribute(ITEMS_DTO_ATTRIBUTE, itemsDto);
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@ModelAttribute(ITEMS_DTO_ATTRIBUTE) @Valid CartItemsUpdateDto itemsUpdateDto,
                             BindingResult br, Model model) {
        List<String> validationErrors = new ArrayList<>(Collections.nCopies(itemsUpdateDto.getItems().size(), null));
        if (br.hasErrors()) {
            for (int i = 0; i < itemsUpdateDto.getItems().size(); i++) {
                if (br.hasFieldErrors(String.format(ERROR_FIELD, i))) {
                    String message = br.getFieldError(String.format(ERROR_FIELD, i)).getDefaultMessage();
                    message = message.startsWith(INVALID_FORMAT_DEFAULT_MESSAGE) ? INVALID_FORMAT_MESSAGE : message;
                    validationErrors.set(i, message);
                }
            }
            model.addAttribute(VALID_ERRORS_ATTRIBUTE, validationErrors);
        } else {
            Map<Long, Long> items = new HashMap<>();
            itemsUpdateDto.getItems().stream()
                    .map(cartItemDto -> Map.of(cartItemDto.getPhoneId(), cartItemDto.getQuantity()))
                    .forEach(items::putAll);
            try {
                cartService.update(items);
            } catch (OutOfStockException e) {
                model.addAttribute(ERROR_ATTRIBUTE, e);
            }
        }
        return "cart";
    }
}
