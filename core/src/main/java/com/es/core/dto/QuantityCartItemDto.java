package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
public class QuantityCartItemDto {
    private long phoneId;
    @NotNull(message = "Quantity is empty")
    @Pattern(regexp = "-?[\\d]+", message = "Quantity must be a number")
    @Min(value = 1l, message = "Quantity must be more than 0")
    private String quantity;
}
