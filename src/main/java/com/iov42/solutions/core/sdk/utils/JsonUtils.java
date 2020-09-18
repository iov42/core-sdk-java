package com.iov42.solutions.core.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    private static final Gson PARSER = new GsonBuilder()
            .serializeNulls()
            .create();

    private JsonUtils() {
        // static usage only
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return PARSER.fromJson(json, clazz);
    }

    public static String toJson(Object pojo) {
        return PARSER.toJson(pojo);
    }
}
