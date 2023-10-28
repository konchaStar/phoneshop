package com.es.core.enums;

import java.util.Arrays;

public enum SortType {
    BRAND, MODEL, DISPLAYSIZEINCHES, PRICE;
    public static SortType getValue(String name) {
        return Arrays.stream(SortType.values())
                .filter(value -> value.toString().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }
}
