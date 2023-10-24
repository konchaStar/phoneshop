package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.rowmapper.OrderItemRowMapper;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String UPDATE_STOCKS = "update stocks set stock=:stock, reserved=:reserved where phoneId=:phoneId";
    private static final String SELECT_ORDER = "select * from orders where secureId = ?";
    private static final String SELECT_ITEM = "select * from orderItems where orderId = ?";
    private static final String ID = "id";
    private static final String ORDERS_TABLE = "orders";
    private static final String ORDER_ITEMS_TABLE = "orderItems";
    private static final String PHONE_ID = "phoneId";
    private static final String QUANTITY = "quantity";
    private static final String ORDER_ID = "orderId";
    @Resource
    OrderItemRowMapper orderItemRowMapper;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private StockDao stockDao;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setDeliveryPrice(deliveryPrice);
        order.setOrderItems(cart.getPhones().entrySet().stream()
                .map(entry -> new OrderItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalPrice());
        order.setTotalPrice(order.getDeliveryPrice().add(order.getSubtotal()));
        return order;
    }

    @Transactional
    @Override
    public void placeOrder(Order order) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        order.setSecureId(UUID.randomUUID().toString());
        Long orderId = insert.withTableName(ORDERS_TABLE).usingGeneratedKeyColumns(ID)
                .executeAndReturnKey(new BeanPropertySqlParameterSource(order)).longValue();
        order.setId(orderId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        for (OrderItem item : order.getOrderItems()) {
            insert = new SimpleJdbcInsert(jdbcTemplate);
            item.setOrderId(orderId);
            insert.withTableName(ORDER_ITEMS_TABLE).usingGeneratedKeyColumns(ID)
                    .execute(Map.of(PHONE_ID, item.getPhone().getId(), QUANTITY, item.getQuantity(),
                            ORDER_ID, item.getOrderId()));
            Stock stock = stockDao.getAvailableStock(item.getPhone().getId());
            Integer reserved = stock.getReserved() + item.getQuantity().intValue();
            stock.setReserved(reserved);
            template.batchUpdate(UPDATE_STOCKS,
                    new SqlParameterSource[]{new BeanPropertySqlParameterSource(stock)});
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        Order order = jdbcTemplate.queryForObject(SELECT_ORDER, new Object[]{secureId},
                new BeanPropertyRowMapper<>(Order.class));
        List<OrderItem> orderItems = jdbcTemplate.query(SELECT_ITEM,
                new Object[]{order.getId()}, orderItemRowMapper);
        order.setOrderItems(orderItems);
        return order;
    }
}
