package com.es.core.enums;

import java.util.Arrays;

public enum SortType {
    brand, model, displaySizeInches, price;
    public static SortType getValue(String name) {
        return Arrays.stream(SortType.values())
                .filter(value -> value.toString().equals(name))
                .findAny()
                .orElse(null);
    }
}
