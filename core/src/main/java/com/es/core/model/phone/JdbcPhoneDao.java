package com.es.core.model.phone;

import com.es.core.enums.SortOrder;
import com.es.core.enums.SortType;
import com.es.core.model.rowmapper.PhoneRowMapper;
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

@Component
public class JdbcPhoneDao implements PhoneDao {
    private final static String SELECT_PHONE_QUERY = "select * from phones where id = ?";
    private final static String DELETE_PHONE2COLOR_QUERY = "delete from phone2color where phoneId = ?";
    private final static String INSERT_PHONE2COLOR_QUERY = "insert into phone2color (phoneId, colorId) values(:phoneId, :colorId)";
    private final static String OFFSET_LIMIT = "offset ? limit ?";
    private final static String SELECT_PHONES_JOIN_STOCK = "select * from phones join stocks on phones.id " +
            "= stocks.phoneId where stocks.stock - stocks.reserved > 0";
    private final static String SELECT_PHONE_OFFSET_QUERY = SELECT_PHONES_JOIN_STOCK + OFFSET_LIMIT;
    private final static String UPDATE_QUERY = "update phones set id = :id, brand = :brand, model = :model, price = :price," +
            " displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm," +
            " widthMm = :widthMm, heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os," +
            " displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, " +
            "standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, " +
            "description = :description where id = :id";
    private final static String SELECT_PHONES_COUNT_QUERY = "select count(phones.id) from phones join " +
            "stocks on phones.id = stocks.phoneId where stocks.stock - stocks.reserved > 0";
    private final static String LIKE_MODEL_CONDITION = "lower(model) like ? ";
    private final static String ORDER_BY = " order by ";
    private final static String PHONES_TABLE = "phones";
    private final static String COLOR_ID_COLUMN = "colorId";
    private final static String PHONE_ID_COLUMN = "phoneId";
    private final static String ID_COLUMN = "id";
    private final static String OR = " or ";
    private final static String AND = " and ";
    private final static String PERCENT = "%";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private PhoneRowMapper phoneRowMapper;
    public Optional<Phone> get(final Long key) {
        Object[] keyArg = new Object[]{key};
        return jdbcTemplate.query(SELECT_PHONE_QUERY,
                        keyArg, phoneRowMapper)
                .stream()
                .findFirst();
    }


    public void save(final Phone phone) {
        boolean notExisted = jdbcTemplate.query(SELECT_PHONE_QUERY, new Object[]{phone.getId()},
                new BeanPropertyRowMapper<>(Phone.class)).isEmpty();
        if (notExisted) {
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
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        template.batchUpdate(INSERT_PHONE2COLOR_QUERY,
                phone.getColors().stream()
                        .map(color -> new MapSqlParameterSource(Map.of(PHONE_ID_COLUMN, phone.getId(),
                                COLOR_ID_COLUMN, color.getId()))
                        ).toArray(SqlParameterSource[]::new));
    }

    public List<Phone> findAll(String search, SortType type, SortOrder sortOrder, int offset, int limit) {
        List<Phone> phones;
        String sort = type == null ? "" : type.toString();
        String order = sortOrder == null ? "" : sortOrder.toString();
        if (!search.isEmpty() || !sort.isEmpty()) {
            List<Object> args = new ArrayList<>();

            String[] words = search.toLowerCase().split("//s");
            String query = getSelectSearchSortQuery(words, sort, order);
            if (!search.isEmpty()) {
                Arrays.stream(words)
                        .map(word -> PERCENT.concat(word).concat(PERCENT))
                        .forEach(args::add);
            }
            args.add(offset);
            args.add(limit);
            phones = jdbcTemplate.query(query, args.toArray(), phoneRowMapper);
        } else {
            Object[] offsetLimitArg = new Object[]{offset, limit};
            phones = jdbcTemplate.query(SELECT_PHONE_OFFSET_QUERY, offsetLimitArg,
                    phoneRowMapper);
        }
        return phones;
    }

    private String getSelectSearchSortQuery(String[] words, String sort, String order) {
        StringBuilder query = new StringBuilder(SELECT_PHONES_JOIN_STOCK);
        if (words.length > 0 && !words[0].isBlank()) {
            query.append(AND).append("(");
            query.append(LIKE_MODEL_CONDITION);
            for (int i = 1; i < words.length; i++) {
                query.append(OR);
                query.append(LIKE_MODEL_CONDITION);
            }
            query.append(")");
        }
        if (!sort.isEmpty()) {
            query.append(ORDER_BY).append(sort).append(" ").append(order).append(" ");
        }
        query.append(OFFSET_LIMIT);
        return query.toString();
    }

    @Override
    public Long getRowCount(String search) {
        if (search.isBlank()) {
            return jdbcTemplate.queryForObject(SELECT_PHONES_COUNT_QUERY, Long.class);
        } else {
            StringBuilder query = new StringBuilder(SELECT_PHONES_COUNT_QUERY);
            List<Object> args = new ArrayList<>();
            String[] words = search.split("//s");
            query.append(AND).append("(");
            query.append(LIKE_MODEL_CONDITION);
            args.add(PERCENT.concat(words[0]).concat(PERCENT));
            for (int i = 1; i < words.length; i++) {
                query.append(OR);
                query.append(LIKE_MODEL_CONDITION);
                args.add(PERCENT.concat(words[i]).concat(PERCENT));
            }
            query.append(")");
            return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Long.class);
        }
    }
}
