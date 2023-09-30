package com.es.core.model;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-coreTest.xml")
public class JdbcPhoneDaoIntTest {
    @Autowired
    private PhoneDao phoneDao;
    private Color black;
    private Color purple;
    private Color green;
    @Before
    public void init() {
        black = new Color();
        black.setCode("Black");
        black.setId(1000L);
        purple = new Color();
        purple.setCode("Purple");
        purple.setId(1005L);
        green = new Color();
        green.setId(1007L);
        green.setCode("Green");
    }

    @Test
    public void getPhoneTest() {
        Optional<Phone> phone = phoneDao.get(1000L);
        Optional<Phone> expectedPhone = Optional.of(new Phone(1000L, "ARCHOS", "ARCHOS 101 G9", null,
                BigDecimal.valueOf(10.1), 482, BigDecimal.valueOf(276.0), BigDecimal.valueOf(167.0),
                BigDecimal.valueOf(12.6), null,"Tablet", "Android (4.0)", Set.of(black, purple), "1280 x  800", 149, null,
                null, BigDecimal.valueOf(1.3), null, BigDecimal.valueOf(8.0), null,
                null, null, "2.1, EDR", "GPS",
                "manufacturer/ARCHOS/ARCHOS 101 G9.jpg", "The ARCHOS 101 G9 is a 10.1'' " +
                "tablet, equipped with Google's open source OS. It offers a multi-core ARM CORTEX" +
                " A9 processor at 1GHz, 8 or 16GB internal memory, microSD card slot, GPS, Wi-Fi, Bluetooth 2.1, and more."));
        Assert.assertEquals(expectedPhone, phone);
    }

    @Test
    public void saveExistedPhoneTest() {
        Phone phone = phoneDao.get(1000L).get();
        phone.setBrand("Samsung");
        Set<Color> colors = Set.of(green);
        phone.setColors(colors);
        phoneDao.save(phone);
        Phone actualPhone = phoneDao.get(1000L).get();
        Assert.assertEquals(phone, actualPhone);
    }

    @Test
    public void saveNewPhoneTest() {
        Phone phone = phoneDao.get(1000L).get();
        phone.setId(1100L);
        phone.setModel("Samsung galaxy");
        phone.setBrand("Samsung");
        phoneDao.save(phone);
        Long expectedId = 1005L;
        Phone actualPhone = phoneDao.get(expectedId).get();
        Assert.assertEquals(phone, actualPhone);
    }

    @Test
    public void findAllTest() {
        List<Phone> actualPhones = phoneDao.findAll("", "", "", 0, 4);
        List<Phone> expectedPhones = List.of(phoneDao.get(1000L).get(), phoneDao.get(1001L).get(),
                phoneDao.get(1002L).get(), phoneDao.get(1003L).get());
        Assert.assertEquals(expectedPhones, actualPhones);
    }
}
