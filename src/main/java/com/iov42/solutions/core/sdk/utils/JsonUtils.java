package com.iov42.solutions.core.sdk.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper PARSER = new ObjectMapper();

    private JsonUtils() {
        // static usage only
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return PARSER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toJson(Object pojo) {
        try {
            return PARSER.writeValueAsString(pojo);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
