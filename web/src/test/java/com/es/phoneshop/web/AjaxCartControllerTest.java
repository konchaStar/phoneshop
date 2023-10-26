package com.es.phoneshop.web;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.dto.QuantityAddToCartDto;
import com.es.core.dto.QuantityCartItemDto;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.stock.StockDao;
import com.es.phoneshop.web.controller.AjaxCartController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

public class AjaxCartControllerTest {
    @Mock
    private BindingResult br;
    @Mock
    private Cart cart;
    @Mock
    private CartService cartService;
    @InjectMocks
    private AjaxCartController controller;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void AjaxCartControllerAddPhoneTest() {
        QuantityCartItemDto itemDto = new QuantityCartItemDto(1001L, 2L);
        when(br.hasErrors()).thenReturn(false);
        when(cartService.getCart()).thenReturn(cart);
        QuantityAddToCartDto result = controller.addPhone(itemDto, br);
        verify(cartService).addPhone(1001L, 2L);
        Assert.assertEquals(result.getMessage(), "Phone was successfully added");
    }
}
