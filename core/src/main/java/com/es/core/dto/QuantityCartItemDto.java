package com.es.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityCartItemDto {
    private Long phoneId;
    @NotNull(message = "Quantity is empty")
    @Min(value = 1l, message = "Quantity must be more than 0")
    private Long quantity;
}