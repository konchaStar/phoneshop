package com.es.core.model.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdToLongRowMapper implements RowMapper<Long> {
    private String columnName;
    public IdToLongRowMapper(String column) {
        this.columnName = column;
    }
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long number = rs.getLong(columnName);
        return number;
    }
}
