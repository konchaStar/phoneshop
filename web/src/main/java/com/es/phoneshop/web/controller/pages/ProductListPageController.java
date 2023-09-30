package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao phoneDao;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "search", defaultValue = "") String search,
                                  @RequestParam(name = "sort", defaultValue = "") String sort,
                                  @RequestParam(name = "order", defaultValue = "") String order) {
        model.addAttribute("phones", phoneDao.findAll(search, sort, order, (page - 1) * 10, 10));
        Long pages = phoneDao.getNumberOfPages(search);
        model.addAttribute("pages", pages);
        return "productList";
    }
}
