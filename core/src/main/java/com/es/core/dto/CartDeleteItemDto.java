package com.es.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartDeleteItemDto {
    private Long phoneId;
    private Long totalQuantity;
    private BigDecimal totalPrice;
}
