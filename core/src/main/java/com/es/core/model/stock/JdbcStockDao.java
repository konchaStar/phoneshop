package com.es.core.model.stock;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JdbcStockDao implements StockDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Stock getAvailableStock(Long phoneId) {
        return (Stock) jdbcTemplate.query("select stock, reserved from stocks where phoneId = ?", new Object[]{phoneId},
                new BeanPropertyRowMapper(Stock.class)).get(0);
    }
}
