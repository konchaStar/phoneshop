package com.es.core.model.rowmapper;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class PhoneRowMapper implements RowMapper<Phone> {
    private final static String SELECT_COLOR_JOIN_QUERY = "select colors.id, colors.code from colors join phone2color" +
            " on phone2color.colorId = colors.id where phone2color.phoneId = ?";
    private JdbcTemplate jdbcTemplate;

    public PhoneRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Phone mapRow(ResultSet rs, int rowNum) throws SQLException {
        Phone phone = new BeanPropertyRowMapper<>(Phone.class).mapRow(rs, rowNum);
        Set<Color> colors = getColorSet(phone);
        phone.setColors(colors);
        return phone;
    }

    private Set<Color> getColorSet(final Phone phone) {
        return jdbcTemplate.query(SELECT_COLOR_JOIN_QUERY,
                        new Object[]{phone.getId()}, new BeanPropertyRowMapper<>(Color.class))
                .stream()
                .collect(Collectors.toSet());
    }
}
