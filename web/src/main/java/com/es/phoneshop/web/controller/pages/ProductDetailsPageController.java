package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    @Resource
    private PhoneDao phoneDao;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public String showPhone(Model model, @PathVariable Long id) {
        Optional<Phone> phone = phoneDao.get(id);
        if (phone.isPresent()) {
            model.addAttribute("phone", phone.get());
            return "productDetails";
        }
        throw new ProductNotFoundException(String.format("Product with id %d not found", id));
    }
}
