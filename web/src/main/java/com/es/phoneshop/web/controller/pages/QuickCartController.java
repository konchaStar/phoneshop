package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.dto.PhoneModelQuantityDto;
import com.es.core.dto.QuickCartItemsDto;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quickCart")
public class QuickCartController {
    private static final String QUICK_CART_ITEMS_ATTRIBUTE = "quickCartItemsDto";
    private static final String SUCCESS_MESSAGE = "%s successfully added";
    private static final String PHONE_ERRORS_ATTRIBUTE = "phoneErrors";
    private static final String QUANTITY_ERRORS_ATTRIBUTE = "quantityErrors";
    private static final String SUCCESS_MESSAGE_ATTRIBUTE = "successMessages";
    private static final String QUANTITY_ERROR_FIELD = "quickCartItems[%d].quantity";
    private static final String INVALID_FORMAT_DEFAULT_MESSAGE = "Failed to convert property value";
    private static final String INVALID_FORMAT_MESSAGE = "Quantity must be number";
    private static final String QUANTITY_EMPTY_ERROR = "Quantity is empty";
    private static final String PHONE_EMPTY_ERROR = "Phone is empty";
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max quantity %d";
    private static final String PHONE_NOT_FOUND_ERROR = "Phone not found";
    @Resource
    private CartService cartService;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

    @GetMapping
    public String showQuickCart(Model model) {
        QuickCartItemsDto quickCartItemsDto = new QuickCartItemsDto();
        model.addAttribute(QUICK_CART_ITEMS_ATTRIBUTE, quickCartItemsDto);
        return "quickCart";
    }

    @PostMapping
    public String addToCart(@ModelAttribute(QUICK_CART_ITEMS_ATTRIBUTE) @Valid QuickCartItemsDto quickCartItemsDto,
                            BindingResult br, Model model) {
        List<String> phoneErrors = new ArrayList<>(Collections.nCopies(quickCartItemsDto.getQuickCartItems().size(),
                null));
        List<String> quantityErrors = new ArrayList<>(Collections.nCopies(quickCartItemsDto.getQuickCartItems().size(),
                null));
        List<String> successMessages = new ArrayList<>();
        List<Phone> validPhones = handleErrors(phoneErrors, quantityErrors, quickCartItemsDto, br);
        for (int i = 0; i < validPhones.size(); i++) {
            Phone phone = validPhones.get(i);
            if (phone != null) {
                cartService.addPhone(phone.getId(), quickCartItemsDto.getQuickCartItems().get(i).getQuantity());
                successMessages.add(String.format(SUCCESS_MESSAGE, phone.getModel()));
                quickCartItemsDto.getQuickCartItems().set(i, null);
            }
        }
        model.addAttribute(PHONE_ERRORS_ATTRIBUTE, phoneErrors);
        model.addAttribute(QUANTITY_ERRORS_ATTRIBUTE, quantityErrors);
        model.addAttribute(SUCCESS_MESSAGE_ATTRIBUTE, successMessages);
        return "quickCart";
    }

    private List<Phone> handleErrors(List<String> phoneErrors, List<String> quantityErrors, QuickCartItemsDto quickCartItemsDto,
                                     BindingResult br) {
        List<PhoneModelQuantityDto> phoneModelQuantityDtos = quickCartItemsDto.getQuickCartItems();
        List<Phone> validPhones = new ArrayList<>(Collections.nCopies(phoneErrors.size(), null));
        for (int i = 0; i < phoneModelQuantityDtos.size(); i++) {
            if (!(phoneModelQuantityDtos.get(i).getQuantity() == null &&
                    phoneModelQuantityDtos.get(i).getModel().isBlank())) {
                if (br.hasFieldErrors(String.format(QUANTITY_ERROR_FIELD, i))) {
                    String error = br.getFieldError(String.format(QUANTITY_ERROR_FIELD, i)).getDefaultMessage();
                    error = error.startsWith(INVALID_FORMAT_DEFAULT_MESSAGE) ? INVALID_FORMAT_MESSAGE : error;
                    quantityErrors.set(i, error);
                } else {
                    if (phoneModelQuantityDtos.get(i).getQuantity() == null) {
                        quantityErrors.set(i, QUANTITY_EMPTY_ERROR);
                    } else if (phoneModelQuantityDtos.get(i).getModel().isBlank()) {
                        phoneErrors.set(i, PHONE_EMPTY_ERROR);
                    } else {
                        Optional<Phone> optionalPhone = phoneDao.getByModel(phoneModelQuantityDtos.get(i).getModel());
                        if (optionalPhone.isPresent()) {
                            Stock stock = stockDao.getAvailableStock(optionalPhone.get().getId());
                            Integer cartQuantity = Optional.ofNullable(cartService.getCart().getPhones()
                                    .get(optionalPhone.get())).orElse(0);
                            if (stock.getStock() - stock.getReserved() - cartQuantity
                                    - phoneModelQuantityDtos.get(i).getQuantity() < 0) {
                                quantityErrors.set(i, String.format(OUT_OF_STOCK_ERROR,
                                        stock.getStock() - stock.getReserved() - cartQuantity));
                            } else {
                                validPhones.set(i, optionalPhone.get());
                            }
                        } else {
                            phoneErrors.set(i, PHONE_NOT_FOUND_ERROR);
                        }
                    }
                }
            }
        }
        return validPhones;
    }
}
