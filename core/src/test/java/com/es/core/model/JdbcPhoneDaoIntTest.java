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
        String expectedModel = "ARCHOS 101 G9";
        Assert.assertEquals(expectedModel, phone.get().getModel());
        for (Color color : phone.get().getColors()) {
            Assert.assertTrue(color.getId().equals(black.getId())
                    || color.getId().equals(purple.getId()));
        }
        Assert.assertEquals(2, phone.get().getColors().size());
    }

    @Test
    public void saveExistedPhoneTest() {
        Phone phone = phoneDao.get(1000L).get();
        phone.setBrand("Samsung");
        Set<Color> colors = Set.of(green);
        phone.setColors(colors);
        phoneDao.save(phone);
        Phone actualPhone = phoneDao.get(1000L).get();
        Assert.assertEquals(phone.getModel(), actualPhone.getModel());
        for (Color color : actualPhone.getColors()) {
            Assert.assertTrue(color.getId().equals(green.getId()));
        }
        Assert.assertEquals(1, actualPhone.getColors().size());
    }

    @Test
    public void saveNewPhoneTest() {
        Phone phone = phoneDao.get(1000L).get();
        phone.setId(1100L);
        phone.setModel("Samsung galaxy");
        phone.setBrand("Samsung");
        phoneDao.save(phone);
        Phone actualPhone = phoneDao.get(1100L).get();
        Assert.assertEquals(phone.getId(), actualPhone.getId());
    }

    @Test
    public void findAllTest() {
        List<Phone> phones = phoneDao.findAll(0, 4);
        Assert.assertEquals(4, phones.size());
        for (Color color : phones.get(0).getColors()) {
            Assert.assertTrue(color.getId().equals(black.getId())
                    || color.getId().equals(purple.getId()));
        }
    }
}
