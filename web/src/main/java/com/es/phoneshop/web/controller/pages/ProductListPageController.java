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
    private final static String SELECT_COUNT_PHONES_JOIN_STOCKS = "select count(phones.id) from phones join " +
            "stocks on phones.id = stocks.phoneId where stocks.stock - stocks.reserved > 0 and ";
    private final static String SELECT_PHONES_COUNT_QUERY = "select count(phones.id) from phones join " +
            "stocks on phones.id = stocks.phoneId where stocks.stock - stocks.reserved > 0";
    private final static String LIKE_MODEL_CONDITION = "lower(model) like ? or ";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private PhoneDao phoneDao;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "search", defaultValue = "") String search,
                                  @RequestParam(name = "sort", defaultValue = "") String sort,
                                  @RequestParam(name = "order", defaultValue = "") String order) {
        SortType type = SortType.getValue(sort);
        SortOrder sortOrder = SortOrder.getValue(order);
        model.addAttribute("phones", phoneDao.findAll(search, type, sortOrder, (page - 1) * 10, 10));
        Long pages = getNumberOfPages(search);
        model.addAttribute("pages", pages);
        return "productList";
    }

    public Long getNumberOfPages(String search) {
        if (search.isEmpty()) {
            return (jdbcTemplate.queryForObject(SELECT_PHONES_COUNT_QUERY, Long.class) + 9) / 10;
        } else {
            StringBuilder query = new StringBuilder(SELECT_COUNT_PHONES_JOIN_STOCKS);
            List<Object> args = new ArrayList<>();
            for (String word : search.toLowerCase().split("//s")) {
                query.append(LIKE_MODEL_CONDITION);
                args.add("%".concat(word).concat("%"));
            }
            query.delete(query.length() - 3, query.length());
            return (jdbcTemplate.queryForObject(query.toString(), args.toArray(), Long.class) + 9) / 10;
        }
    }
}
