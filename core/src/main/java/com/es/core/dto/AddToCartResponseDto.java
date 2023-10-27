package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AddToCartResponseDto {
    private String message;
    private boolean errorStatus;
    private long totalQuantity;
    private BigDecimal totalPrice;
    private long phoneId;
}
