package com.es.core.model.stock;

public interface StockDao {
    Stock getAvailableStock(Long phoneId);
}
