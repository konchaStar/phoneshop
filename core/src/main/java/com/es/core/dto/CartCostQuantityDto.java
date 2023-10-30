package com.es.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartCostQuantityDto {
    private Integer totalQuantity;
    private BigDecimal totalPrice;
}
