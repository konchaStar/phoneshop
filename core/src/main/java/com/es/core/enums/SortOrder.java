package com.es.core.enums;

import java.util.Arrays;

public enum SortOrder {
    ASC, DESC;
    public static SortOrder getValue(String name) {
        return Arrays.stream(SortOrder.values())
                .filter(value -> value.toString().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }
}
