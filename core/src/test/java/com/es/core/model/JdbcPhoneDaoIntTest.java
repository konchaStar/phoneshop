package com.es.core.model;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-coreTest.xml")
public class JdbcPhoneDaoIntTest {
    @Autowired
    private PhoneDao phoneDao;

    @Test
    public void getPhoneTest() {
        Optional<Phone> phone = phoneDao.get(1000L);
        String expectedModel = "ARCHOS 101 G9";
        Color black = new Color();
        black.setCode("Black");
        black.setId(1000L);
        Color purple = new Color();
        purple.setCode("Purple");
        purple.setId(1005L);
        Assert.assertEquals(expectedModel, phone.get().getModel());
        for (Color color : phone.get().getColors()) {
            Assert.assertTrue(color.equals(black) || color.equals(purple));
        }
        Assert.assertEquals(2, phone.get().getColors().size());
    }

    @Test
    public void saveExistedPhoneTest() {
        Phone phone = phoneDao.get(1000L).get();
        phone.setBrand("Samsung");
        phoneDao.save(phone);
        Phone actualPhone = phoneDao.get(1000L).get();
        Assert.assertEquals(phone.getModel(), actualPhone.getModel());
    }
    @Test
    public void saveNewPhone() {
        Phone phone = phoneDao.get(1000L).get();
        phone.setId(1100L);
        phoneDao.save(phone);
        Phone actualPhone = phoneDao.get(1100L).get();
        Assert.assertTrue(actualPhone.equals(phone));
    }
}
