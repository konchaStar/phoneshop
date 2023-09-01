package com.es.core.model.phone;

import com.es.core.model.rowmapper.IdToLongRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private final String colorsField = "colors";

    public Optional<Phone> get(final Long key) {
        Optional<Phone> phone = jdbcTemplate.query("select * from phones where id = '" + key + "'", new BeanPropertyRowMapper(Phone.class))
                .stream()
                .findFirst();
        if (phone.isPresent()) {
            List<Long> colorIds = jdbcTemplate.query("select colorId from phone2color where phoneId = '" + key + "'",
                    new IdToLongRowMapper("colorId"));
            Set<Color> colors = new HashSet<>();
            for (Long colorId : colorIds) {
                colors.add(jdbcTemplate.query("select * from colors where id = '" + colorId + "'", new BeanPropertyRowMapper<>(Color.class))
                        .stream()
                        .findFirst()
                        .get());
            }
            phone.get().setColors(colors);
        }
        return phone;
    }

    public void save(final Phone phone) {
        if (jdbcTemplate.query("select * from phones where id = '" + phone.getId() + "'",
                new BeanPropertyRowMapper<>(Phone.class)).isEmpty()) {
            String query = getInsertQuery(phone);
            jdbcTemplate.update(query);
        } else {
            String query = getUpdateQuery(phone);
            jdbcTemplate.update(query);
        }
    }

    private Map<String, Object> getPhoneFieldsValuesMap(final Phone phone) {
        Class<?> phoneClass = phone.getClass();
        Map<String, Object> fieldsValues = new HashMap<>();
        for (Method method : phoneClass.getMethods()) {
            if (method.getName().startsWith("get") && !(method.getReturnType().equals(Set.class) || method.getReturnType().equals(Class.class))) {
                try {
                    fieldsValues.put(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4),
                            method.invoke(phone));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fieldsValues;
    }

    private String getInsertQuery(final Phone phone) {
        Map<String, Object> fieldsValues = getPhoneFieldsValuesMap(phone);
        StringBuilder builder = new StringBuilder("insert into phones (");
        for (String field : fieldsValues.keySet()) {
            builder.append(field + ", ");
        }
        builder.delete(builder.length() - 2, builder.length()).append(") values (");
        for (String field : fieldsValues.keySet()) {
            Object value = fieldsValues.get(field);
            builder.append(value != null && value.getClass().equals(String.class) ? "'" : "");
            if (value != null && value.getClass().equals(String.class)) {
                builder.append(shielding((String) value));
            } else {
                builder.append(value);
            }
            builder.append(value != null && value.getClass().equals(String.class) ? "'" : "");
            builder.append(", ");
        }
        builder.delete(builder.length() - 2, builder.length()).append(")");
        return builder.toString();
    }

    private String getUpdateQuery(final Phone phone) {
        Map<String, Object> fieldsValues = getPhoneFieldsValuesMap(phone);
        StringBuilder builder = new StringBuilder("update phones set ");
        for (String field : fieldsValues.keySet()) {
            Object value = fieldsValues.get(field);
            builder.append(field);
            builder.append(" = " + (value != null && value.getClass().equals(String.class) ? "'" : ""));
            if (value != null && value.getClass().equals(String.class)) {
                builder.append(shielding((String) value));
            } else {
                builder.append(fieldsValues.get(field));
            }
            builder.append((value != null && value.getClass().equals(String.class) ? "'" : "") + ", ");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append("where id = " + phone.getId());
        return builder.toString();
    }

    private String shielding(String str) {
        int index = 0;
        int apostropheIndex = str.indexOf('\'', index);
        while (apostropheIndex > 0) {
            str = str.substring(0, apostropheIndex) + '\'' + str.substring(apostropheIndex);
            index = apostropheIndex + 2;
            apostropheIndex = str.indexOf('\'', index);
        }
        return str;
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query("select * from phones offset " + offset + " limit " + limit, new BeanPropertyRowMapper(Phone.class));
    }
}
