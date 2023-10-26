package com.es.core.model;

import com.es.core.cart.Cart;
import com.es.core.cart.HttpSessionCartService;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ObjectFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HttpCartSessionServiceTest {
    private Cart cart;
    @Mock
    private ObjectFactory<Cart> cartObjectFactory;
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private StockDao stockDao;
    @InjectMocks
    private HttpSessionCartService cartService;
    @Before
    public void init() {
        cart = new Cart();
        cart.setPhones(new HashMap<>());
        MockitoAnnotations.openMocks(this);
        when(cartService.getCart()).thenReturn(cart);
        Phone phone = new Phone();
        phone.setPrice(BigDecimal.valueOf(100L));
        when(phoneDao.get(any())).thenReturn(Optional.of(phone));
        Stock stock = new Stock();
        stock.setReserved(1);
        stock.setStock(10);
        when(stockDao.getAvailableStock(any())).thenReturn(stock);
    }
    @Test
    public void cartServiceAddPhoneTest() {
        cartService.addPhone(1001L, 2L);
        Assert.assertEquals(2L, cartService.getCart().getTotalQuantity().longValue());
        Assert.assertEquals(200L, cart.getTotalPrice().longValue());
    }
    @Test
    public void cartServiceClear() {
        cartService.addPhone(1001L, 2L);
        cartService.clear();
        Assert.assertEquals(0L, cartService.getCart().getTotalQuantity().longValue());
        Assert.assertEquals(0L, cartService.getCart().getTotalPrice().longValue());
    }
}