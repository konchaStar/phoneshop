package com.es.core.model.phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(String search, String sort, String order, int offset, int limit);

    Long getNumberOfPages(String search);
}
