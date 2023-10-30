package com.es.core.model.phone;

import com.es.core.enums.SortOrder;
import com.es.core.enums.SortType;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(String search, SortType type, SortOrder sortOrder, int offset, int limit);

    Long getRowCount(String search);
}
