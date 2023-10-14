package com.es.core.dto;

import com.es.core.cart.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CartItemsUpdateDto {
    @Valid
    private List<QuantityCartItemDto> items;
    public void copyFromCart(Cart cart) {
        items = cart.getPhones().keySet()
                .stream()
                .map(phone -> new QuantityCartItemDto(phone.getId(), cart.getPhones().get(phone)))
                .collect(Collectors.toList());
    }
}
