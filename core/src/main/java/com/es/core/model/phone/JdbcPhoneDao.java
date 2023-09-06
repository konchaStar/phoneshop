package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private final static String SELECT_PHONE_QUERY = "select * from phones where id = ?";
    private final static String SELECT_COLOR_JOIN_QUERY = "select colors.id, colors.code from colors join phone2color" +
            " on phone2color.colorId = colors.id where phone2color.phoneId = ?";
    private final static String DELETE_PHONE2COLOR_QUERY = "delete from phone2color where phoneId = ?";
    private final static String INSERT_PHONE2COLOR_QUERY = "insert into phone2color (phoneId, colorId) values(:phoneId, :colorId)";
    private final static String SELECT_PHONE_OFFSET_QUERY = "select * from phones offset ? limit ?";
    private final static String UPDATE_QUERY = "update phones set id = :id, brand = :brand, model = :model, price = :price," +
            " displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm," +
            " widthMm = :widthMm, heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os," +
            " displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, " +
            "standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, " +
            "description = :description where id = :id";
    private final static String PHONES_TABLE = "phones";
    private final static String COLOR_ID_COLUMN = "colorId";
    private final static String PHONE_ID_COLUMN = "phoneId";
    private final static String ID_COLUMN = "id";
    @Resource
    private JdbcTemplate jdbcTemplate;

    public Optional<Phone> get(final Long key) {
        Optional<Phone> phone = jdbcTemplate.query(SELECT_PHONE_QUERY,
                        new Object[]{key}, new BeanPropertyRowMapper(Phone.class))
                .stream()
                .findFirst();
        if (phone.isPresent()) {
            Set<Color> colors = getColorSet(phone.get());
            phone.get().setColors(colors);
        }
        return phone;
    }

    private Set<Color> getColorSet(final Phone phone) {
        return jdbcTemplate.query(SELECT_COLOR_JOIN_QUERY,
                        new Object[]{phone.getId()}, new BeanPropertyRowMapper<>(Color.class))
                .stream()
                .collect(Collectors.toSet());
    }

    public void save(final Phone phone) {
        if (jdbcTemplate.query(SELECT_PHONE_QUERY, new Object[]{phone.getId()},
                new BeanPropertyRowMapper<>(Phone.class)).isEmpty()) {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
            Number number = insert.withTableName(PHONES_TABLE).usingGeneratedKeyColumns(ID_COLUMN)
                    .executeAndReturnKey(new BeanPropertySqlParameterSource(phone));
            phone.setId(number.longValue());
        } else {
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
            template.batchUpdate(UPDATE_QUERY, new SqlParameterSource[]{new BeanPropertySqlParameterSource(phone)});
        }
        saveColors(phone);
    }

    private void saveColors(final Phone phone) {
        jdbcTemplate.update(DELETE_PHONE2COLOR_QUERY, phone.getId());
        List<SqlParameterSource> parameterSources = phone.getColors().stream()
                .map(color -> {
                    return new MapSqlParameterSource(Map.of(PHONE_ID_COLUMN, phone.getId(),
                            COLOR_ID_COLUMN, color.getId()));
                }).collect(Collectors.toList());
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        template.batchUpdate(INSERT_PHONE2COLOR_QUERY,
                parameterSources.toArray(new SqlParameterSource[0]));
    }

    public List<Phone> findAll(int offset, int limit) {
        List<Phone> phones = jdbcTemplate.query(SELECT_PHONE_OFFSET_QUERY, new Object[]{offset, limit},
                new BeanPropertyRowMapper(Phone.class));
        for (Phone phone : phones) {
            Set<Color> colors = getColorSet(phone);
            phone.setColors(colors);
        }
        return phones;
    }
}
