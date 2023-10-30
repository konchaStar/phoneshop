package com.es.core.model.stock;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class JdbcStockDao implements StockDao {
    private static final String SELECT_STOCK_QUERY = "select phoneId, stock, reserved from stocks where phoneId = :id";
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Stock getAvailableStock(Long phoneId) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        return namedParameterJdbcTemplate.queryForObject(SELECT_STOCK_QUERY, Map.of("id", phoneId),
                new BeanPropertyRowMapper<>(Stock.class));
    }
}
