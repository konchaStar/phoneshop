package com.es.phoneshop.web.controller.pages;

import com.es.core.enums.SortOrder;
import com.es.core.enums.SortType;
import com.es.core.model.phone.PhoneDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    private static final int PHONES_PAGE_AMOUNT = 10;
    private static final String PHONES_ATTRIBUTE = "phones";
    private static final String PAGES_ATTRIBUTE = "pages";
    @Resource
    private PhoneDao phoneDao;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "search", defaultValue = "") String search,
                                  @RequestParam(name = "sort", defaultValue = "") String sort,
                                  @RequestParam(name = "order", defaultValue = "") String order) {
        SortType type = SortType.getValue(sort);
        SortOrder sortOrder = SortOrder.getValue(order);
        model.addAttribute(PHONES_ATTRIBUTE, phoneDao.findAll(search, type, sortOrder,
                (page - 1) * PHONES_PAGE_AMOUNT, PHONES_PAGE_AMOUNT));
        Long pages = (phoneDao.getRowCount(search) + PHONES_PAGE_AMOUNT - 1) / PHONES_PAGE_AMOUNT;
        model.addAttribute(PAGES_ATTRIBUTE, pages);
        return "productList";
    }

}
