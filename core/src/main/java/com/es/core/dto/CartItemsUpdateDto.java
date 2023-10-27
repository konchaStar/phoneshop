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
    private List<QuantityPhoneIdDto> items;
    public static CartItemsUpdateDto copyFromCart(Cart cart) {
        CartItemsUpdateDto cartItemsUpdateDto = new CartItemsUpdateDto();
        cartItemsUpdateDto.setItems(cart.getPhones().keySet()
                .stream()
                .map(phone -> new QuantityPhoneIdDto(phone.getId(), cart.getPhones().get(phone)))
                .collect(Collectors.toList()));
        return cartItemsUpdateDto;
    }
}
