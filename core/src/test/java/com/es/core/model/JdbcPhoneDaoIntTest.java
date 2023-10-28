package com.es.core.model;

import com.es.core.enums.SortOrder;
import com.es.core.enums.SortType;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-coreTest.xml")
public class JdbcPhoneDaoIntTest {
    @Resource
    private PhoneDao phoneDao;

    @Test
    public void getPhone_PhoneArchos101G9_Id1000() {
        Optional<Phone> actualPhone = phoneDao.get(1000L);
        Optional<Phone> expectedPhone = getTestPhone();
        Assert.assertEquals(expectedPhone, actualPhone);
    }

    @Test
    public void saveExistedPhone_PhoneWithNameSamsungAndGreenColor_PhoneWithId1000() {
        Phone expectedPhone = getTestPhone().get();
        expectedPhone.setBrand("Samsung");
        Set<Color> colors = Set.of(getTestColor(1007L, "Green"));
        expectedPhone.setColors(colors);
        phoneDao.save(expectedPhone);
        Phone actualPhone = phoneDao.get(1000L).get();
        Assert.assertEquals(expectedPhone, actualPhone);
    }

    @Test
    public void saveNewPhone_PhoneWithNameSamsungAndId1005_PhoneWithNameSamsungAndId1100() {
        Phone expectedPhone = getTestPhone().get();
        expectedPhone.setId(1100L);
        expectedPhone.setModel("Samsung galaxy");
        expectedPhone.setBrand("Samsung");
        phoneDao.save(expectedPhone);
        Long expectedId = 1005L;
        Phone actualPhone = phoneDao.get(expectedId).get();
        Assert.assertEquals(expectedPhone, actualPhone);
    }

    @Test
    public void findAll_ListWith4Phones_NoSearchNoSortNoOrderOffset0Limit4() {
        List<Phone> actualPhones = phoneDao.findAll("", SortType.valueOf(""), SortOrder.valueOf(""), 0, 4);
        List<Phone> expectedPhones = List.of(phoneDao.get(1000L).get(), phoneDao.get(1001L).get(),
                phoneDao.get(1002L).get(), phoneDao.get(1003L).get());
        Assert.assertEquals(expectedPhones, actualPhones);
    }

    private Optional<Phone> getTestPhone() {
        return Optional.of(new Phone(1000L, "ARCHOS", "ARCHOS 101 G9", null,
                BigDecimal.valueOf(10.1), 482, BigDecimal.valueOf(276.0), BigDecimal.valueOf(167.0),
                BigDecimal.valueOf(12.6), null, "Tablet", "Android (4.0)", Set.of(getTestColor(1000L, "Black"), getTestColor(1005L, "Purple")), "1280 x  800", 149, null,
                null, BigDecimal.valueOf(1.3), null, BigDecimal.valueOf(8.0), null,
                null, null, "2.1, EDR", "GPS",
                "manufacturer/ARCHOS/ARCHOS 101 G9.jpg", "The ARCHOS 101 G9 is a 10.1'' " +
                "tablet, equipped with Google's open source OS. It offers a multi-core ARM CORTEX" +
                " A9 processor at 1GHz, 8 or 16GB internal memory, microSD card slot, GPS, Wi-Fi, Bluetooth 2.1, and more."));
    }

    private Color getTestColor(Long id, String code) {
        return new Color(id, code);
    }
}
