package com.es.core.model.rowmapper;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class PhoneRowMapper implements RowMapper<Phone> {
    private final static String SELECT_COLOR_JOIN_QUERY = "select colors.id, colors.code from colors join phone2color" +
            " on phone2color.colorId = colors.id where phone2color.phoneId = ?";
    @Resource
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
        return new HashSet<>(jdbcTemplate.query(SELECT_COLOR_JOIN_QUERY,
                new Object[]{phone.getId()}, new BeanPropertyRowMapper<>(Color.class)));
    }
}
