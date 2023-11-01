package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
public class QuickCartItemsDto {
    @Valid
    private List<PhoneModelQuantityDto> quickCartItems;
}
