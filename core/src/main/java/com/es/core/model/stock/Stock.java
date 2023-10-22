package com.es.core.model.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stock {
    private Long phoneId;
    private Integer stock;
    private Integer reserved;
}
