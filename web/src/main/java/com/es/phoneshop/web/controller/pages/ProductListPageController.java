package com.es.phoneshop.web.controller.pages;

import com.es.core.enums.SortOrder;
import com.es.core.enums.SortType;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/productList")
public class ProductListPageController {
    private static final int PHONES_PAGE_AMOUNT = 10;
    private static final String PHONES_ATTRIBUTE = "phones";
    private static final String PAGES_ATTRIBUTE = "pages";
    @Resource
    private PhoneDao phoneDao;

    @GetMapping
    public String showProductList(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "search", defaultValue = "") String search,
                                  @RequestParam(name = "sort", defaultValue = "") SortType sort,
                                  @RequestParam(name = "order", defaultValue = "") SortOrder order) {
        model.addAttribute(PHONES_ATTRIBUTE, phoneDao.findAll(search, sort, order,
                (page - 1) * PHONES_PAGE_AMOUNT, PHONES_PAGE_AMOUNT));
        Long pages = (phoneDao.getRowCount(search) + PHONES_PAGE_AMOUNT - 1) / PHONES_PAGE_AMOUNT;
        model.addAttribute(PAGES_ATTRIBUTE, pages);
        return "productList";
    }

}
