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
        Phone phone = getTestPhone();
        Stock stock = getTestStock();
        MockitoAnnotations.openMocks(this);

        when(cartService.getCart()).thenReturn(cart);
        when(phoneDao.get(any())).thenReturn(Optional.of(phone));
        when(stockDao.getAvailableStock(any())).thenReturn(stock);
    }
    private Phone getTestPhone() {
        Phone phone = new Phone();
        phone.setPrice(BigDecimal.valueOf(100L));
        return phone;
    }
    private Stock getTestStock() {
        Stock stock = new Stock();
        stock.setReserved(1);
        stock.setStock(10);
        return stock;
    }
    @Test
    public void addPhone_TotalQuantity2_PhoneWithQuantity2() {
        cartService.addPhone(1001L, 2);
        Assert.assertEquals(2L, cartService.getCart().getTotalQuantity().longValue());
        Assert.assertEquals(200L, cart.getTotalPrice().longValue());
    }
    @Test
    public void clear_TotalQuantity0_InitialQuantity2() {
        cartService.addPhone(1001L, 2);
        cartService.clear();
        Assert.assertEquals(0L, cartService.getCart().getTotalQuantity().longValue());
        Assert.assertEquals(0L, cartService.getCart().getTotalPrice().longValue());
    }
}