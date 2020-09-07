package com.iov42.solutions.core.sdk.utils;

import java.util.Objects;

public class StringUtils {

    private StringUtils() {
        // static usage only
    }

    public static boolean isEmpty(String value) {
        return Objects.isNull(value) || value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
}
