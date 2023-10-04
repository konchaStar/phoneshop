package com.es.core.enums;

import java.util.Arrays;

public enum SortOrder {
    asc, desc;
    public static SortOrder getValue(String name) {
        return Arrays.stream(SortOrder.values())
                .filter(value -> value.toString().equals(name))
                .findAny()
                .orElse(null);
    }
}
