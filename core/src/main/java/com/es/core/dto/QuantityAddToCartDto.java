package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAddToCartDto {
    String message;
    boolean errorStatus;
    long totalQuantity;
    long phoneId;
}
