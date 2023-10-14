package com.es.core.dto;

import com.es.core.model.phone.Color;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class CartItemDto {
    private List<Long> ids;
    private List<String> brands;
    private List<String> models;
    private List<Set<Color>> colors;
    private List<BigDecimal> displaySizesInches;
    private List<BigDecimal> prices;
    private List<Long> quantities;
    private Long totalQuantity;
}
