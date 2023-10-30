package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.dto.CartItemsUpdateDto;
import com.es.core.dto.QuantityPhoneIdDto;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartPageController {
    private static final String INVALID_FORMAT_DEFAULT_MESSAGE = "Failed to convert property value";
    private static final String INVALID_FORMAT_MESSAGE = "Quantity must be number";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String ITEMS_DTO_ATTRIBUTE = "itemsDto";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String ERROR_FIELD = "items[%d].quantity";
    private static final String OUT_OF_STOCK_MESSAGE = "Out of stock. Max quantity %d";
    @Resource
    private CartService cartService;
    @Resource
    private StockDao stockDao;

    @GetMapping
    public String getCart(Model model) {
        model.addAttribute(CART_ATTRIBUTE, cartService.getCart());
        model.addAttribute(ITEMS_DTO_ATTRIBUTE, CartItemsUpdateDto.copyFromCart(cartService.getCart()));
        return "cart";
    }

    @PostMapping
    public String updateCart(@ModelAttribute(ITEMS_DTO_ATTRIBUTE) @Valid CartItemsUpdateDto itemsUpdateDto,
                             BindingResult br, Model model) {
        List<String> errors = new ArrayList<>(Collections.nCopies(itemsUpdateDto.getItems().size(), null));
        boolean hasErrors = checkErrors(errors, itemsUpdateDto, br);
        model.addAttribute(ERRORS_ATTRIBUTE, errors);
        Map<Long, Integer> items = new HashMap<>();
        if(hasErrors) {
            for(int i = 0; i < errors.size(); i++) {
                if(errors.get(i) == null) {
                    items.put(itemsUpdateDto.getItems().get(i).getPhoneId(),
                            itemsUpdateDto.getItems().get(i).getQuantity());
                }
            }
            cartService.update(items);
            return "cart";
        }
        items = itemsUpdateDto.getItems().stream()
                .collect(Collectors.toMap(QuantityPhoneIdDto::getPhoneId, QuantityPhoneIdDto::getQuantity));
        cartService.update(items);
        return "redirect:cart";
    }
    private boolean checkErrors(List<String> errors, CartItemsUpdateDto itemsUpdateDto, BindingResult br) {
        boolean hasErrors = false;
        for (int i = 0; i < itemsUpdateDto.getItems().size(); i++) {
            if (br.hasFieldErrors(String.format(ERROR_FIELD, i))) {
                String message = br.getFieldError(String.format(ERROR_FIELD, i)).getDefaultMessage();
                message = message.startsWith(INVALID_FORMAT_DEFAULT_MESSAGE) ? INVALID_FORMAT_MESSAGE : message;
                errors.set(i, message);
                hasErrors = true;
            } else {
                Long id = itemsUpdateDto.getItems().get(i).getPhoneId();
                Integer quantity = itemsUpdateDto.getItems().get(i).getQuantity();
                Stock stock = stockDao.getAvailableStock(id);
                if (stock.getStock() - quantity - stock.getReserved() < 0) {
                    errors.set(i, String.format(OUT_OF_STOCK_MESSAGE, stock.getStock() - stock.getReserved()));
                    hasErrors = true;
                }
            }
        }
        return hasErrors;
    }
}
