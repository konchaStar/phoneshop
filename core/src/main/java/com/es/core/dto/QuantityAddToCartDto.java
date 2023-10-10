package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAddToCartDto {
    private String message;
    private boolean errorStatus;
    private long totalQuantity;
    private long phoneId;
}
