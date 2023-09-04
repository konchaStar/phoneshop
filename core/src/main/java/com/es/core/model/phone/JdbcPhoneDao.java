package com.es.core.model.phone;

import com.es.core.model.rowmapper.IdToLongRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private final static String SELECT_PHONE_QUERY = "select * from phones where id = ?";
    private final static String SELECT_PHONE2_COLOR_QUERY = "select colorId from phone2color where phoneId = ?";
    private final static String SELECT_COLOR_QUERY = "select * from colors where id = ?";
    private final static String DELETE_PHONE2COLOR_QUERY = "delete from phone2color where phoneId = ?";
    private final static String INSERT_PHONE2COLOR_QUERY = "insert into phone2color (phoneId, colorId) values(?, ?)";
    private final static String SELECT_PHONE_OFFSET_QUERY = "select * from phones offset ? limit ?";
    private final static String COLOR_ID_COLUMN = "colorId";
    private final static String PHONES_TABLE = "phones";
    private final static String UPDATE_PHONES_QUERY = "update phones set ";
    @Resource
    private JdbcTemplate jdbcTemplate;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public Optional<Phone> get(final Long key) {
        lock.readLock().lock();
        Optional<Phone> phone = jdbcTemplate.query(SELECT_PHONE_QUERY,
                        new Object[]{key}, new BeanPropertyRowMapper(Phone.class))
                .stream()
                .findFirst();
        if (phone.isPresent()) {
            List<Long> colorIds = jdbcTemplate.query(SELECT_PHONE2_COLOR_QUERY,
                    new Object[]{key}, new IdToLongRowMapper(COLOR_ID_COLUMN));
            Set<Color> colors = new HashSet<>();
            for (Long colorId : colorIds) {
                colors.add(jdbcTemplate.query(SELECT_COLOR_QUERY, new Object[]{colorId},
                                new BeanPropertyRowMapper<>(Color.class))
                        .stream()
                        .findFirst()
                        .get());
            }
            phone.get().setColors(colors);
        }
        lock.readLock().unlock();
        return phone;
    }

    public void save(final Phone phone) {
        lock.writeLock().lock();
        if (jdbcTemplate.query(SELECT_PHONE_QUERY, new Object[]{phone.getId()},
                new BeanPropertyRowMapper<>(Phone.class)).isEmpty()) {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
            insert.withTableName(PHONES_TABLE).execute(new BeanPropertySqlParameterSource(phone));
        } else {
            update(phone);
        }
        saveColor(phone);
        lock.writeLock().unlock();
    }

    private void saveColor(final Phone phone) {
        jdbcTemplate.update(DELETE_PHONE2COLOR_QUERY, phone.getId());
        for (Color color : phone.getColors()) {
            jdbcTemplate.update(INSERT_PHONE2COLOR_QUERY, new Object[]{phone.getId(), color.getId()});
        }
    }

    private void update(final Phone phone) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(phone);
        List<Object> values = new ArrayList<>();
        StringBuilder builder = new StringBuilder(UPDATE_PHONES_QUERY);
        for (String property : parameterSource.getParameterNames()) {
            if (parameterSource.getSqlType(property) != JdbcUtils.TYPE_UNKNOWN) {
                builder.append(property);
                builder.append(" = ?, ");
                values.add(parameterSource.getValue(property));
            }
        }
        values.add(phone.getId());
        builder.deleteCharAt(builder.length() - 2);
        builder.append("where id = ?");
        jdbcTemplate.update(builder.toString(), values.toArray());
    }

    public List<Phone> findAll(int offset, int limit) {
        lock.readLock().lock();
        List<Phone> phones = jdbcTemplate.query(SELECT_PHONE_OFFSET_QUERY, new Object[]{offset, limit},
                new BeanPropertyRowMapper(Phone.class));
        for (Phone phone : phones) {
            List<Long> colorIds = jdbcTemplate.query(SELECT_PHONE2_COLOR_QUERY, new Object[]{phone.getId()},
                    new IdToLongRowMapper(COLOR_ID_COLUMN));
            Set<Color> colors = new HashSet<>();
            for (Long colorId : colorIds) {
                colors.add(jdbcTemplate.query(SELECT_COLOR_QUERY, new Object[]{colorId},
                                new BeanPropertyRowMapper<>(Color.class))
                        .stream()
                        .findFirst()
                        .get());
            }
            phone.setColors(colors);
        }
        lock.readLock().unlock();
        return phones;
    }
}
