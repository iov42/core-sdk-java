package com.iov42.solutions.core.sdk.utils;

import java.nio.charset.StandardCharsets;

public class PlatformUtils {

    private PlatformUtils() {
        // static usage only
    }

    public static String getEncodedHeaderValue(Object value) {
        return SecurityUtils.encodeBase64(JsonUtils.toJson(value).getBytes(StandardCharsets.UTF_8));
    }
}
