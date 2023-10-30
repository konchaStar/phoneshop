package com.es.core.model.rowmapper;

import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.PhoneDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderItemRowMapper implements RowMapper<OrderItem> {
    private static final String PHONE_ID = "phoneId";
    @Resource
    private PhoneDao phoneDao;

    @Override
    public OrderItem mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderItem item = new BeanPropertyRowMapper<>(OrderItem.class).mapRow(resultSet, i);
        Long phoneId = resultSet.getLong(PHONE_ID);
        item.setPhone(phoneDao.get(phoneId).get());
        return item;
    }
}
