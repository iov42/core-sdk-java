package com.iov42.solutions.core.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the supported types of encryption protocols (hash algorithm and encryption algorithm)
 */
public enum ProtocolType {

    SHA256_WITH_ECDSA("SHA256WithECDSA"),
    SHA256_WITH_RSA("SHA256WithRSA");

    ProtocolType(final String value) {
        this.value = value;
    }

    private final String value;

    @JsonValue
    public final String value() {
        return this.value;
    }
}
