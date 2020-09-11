package com.iov42.solutions.core.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iov42.solutions.core.sdk.model.requests.AuthorisedRequest;

import java.util.Objects;
import java.util.UUID;

public class JsonUtils {

    private static final Gson PARSER = new GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
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

    public static <R extends AuthorisedRequest> String toRequestBody(R request) {

        if (Objects.isNull(request.getRequestId())) {
            request.setRequestId(UUID.randomUUID().toString());
        }

        return PARSER.toJson(request);
    }
}
