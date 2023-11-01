package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class PhoneModelQuantityDto {
    private String model;
    @Min(value = 1L, message = "Quantity must be more than 0")
    private Integer quantity;
}
