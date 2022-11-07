package com.iov42.solutions.core.sdk.utils.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iov42.solutions.core.sdk.model.Claims;
import com.iov42.solutions.core.sdk.model.Endorsements;
import com.iov42.solutions.core.sdk.utils.StringUtils;

import java.io.IOException;

/**
 * JSON Serialization helper utilities.
 */
public class JsonUtils {

    private static class ObjectMapperHolder {

        public static final ObjectMapper MAPPER = new ObjectMapper();

        static {
            SimpleModule module = new SimpleModule();
            module.addSerializer(Endorsements.class, new EndorsementsSerializer());
            module.addSerializer(Claims.class, new ClaimsSerializer());
            MAPPER.registerModule(module);
            MAPPER.registerModule(new JavaTimeModule());
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    private JsonUtils() {
        // static usage only
    }

    /**
     * Deserializes a JSON {@code String} to a POJO.
     *
     * @param json  the JSON representation
     * @param clazz Class to be deserialized
     * @param <T>   Type to be deserialized
     * @return deserialized object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(json)) {
                return null;
            }
            return ObjectMapperHolder.MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not deserialize JSON object.", e);
        }
    }

    /**
     * Serializes a POJO to a JSON {@code String} representation.
     *
     * @param pojo object to serialize
     * @return a JSON {@code String} representation
     */
    public static String toJson(Object pojo) {
        try {
            return ObjectMapperHolder.MAPPER.writeValueAsString(pojo);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not serialize to JSON.", e);
        }
    }
}
