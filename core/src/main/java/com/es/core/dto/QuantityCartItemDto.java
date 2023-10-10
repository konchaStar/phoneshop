package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
public class QuantityCartItemDto {
    private long phoneId;
    @NotNull(message = "Quantity is empty")
    @Min(value = 1l, message = "Quantity must be more than 0")
    private Long quantity;
}