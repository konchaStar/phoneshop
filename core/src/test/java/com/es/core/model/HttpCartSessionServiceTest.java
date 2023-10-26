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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HttpCartSessionServiceTest {
    private Cart cart;
    @Mock
    private Map<Phone, Long> phones;
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
    }
    @Test
    public void cartServiceAddPhoneTest() {
        when(cartService.getCart()).thenReturn(cart);
        when(phoneDao.get(any())).thenReturn(Optional.of(new Phone()));
        Stock stock = new Stock();
        stock.setReserved(1);
        stock.setStock(10);
        when(stockDao.getAvailableStock(any())).thenReturn(stock);
        cartService.addPhone(1001L, 2L);
        Assert.assertEquals(2L, cartService.getCart().getTotalQuantity().longValue());
    }
    @Test
    public void cartServiceClear() {
        when(cartService.getCart()).thenReturn(cart);
        when(phoneDao.get(any())).thenReturn(Optional.of(new Phone()));
        Stock stock = new Stock();
        stock.setReserved(1);
        stock.setStock(10);
        when(stockDao.getAvailableStock(any())).thenReturn(stock);
        cartService.addPhone(1001L, 2L);
        cartService.clear();
        Assert.assertEquals(0L, cartService.getCart().getTotalQuantity().longValue());
    }
}