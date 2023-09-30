package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class QuantityCartItemDto {
    @NotEmpty(message = "Quantity is empty")
    @Min(value = 1l, message = "Quantity must be more than 0")
    private long phoneId;
    private long quantity;
}
