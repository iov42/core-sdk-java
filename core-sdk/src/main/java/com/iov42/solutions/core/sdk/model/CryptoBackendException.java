package com.iov42.solutions.core.sdk.model;

/**
 * Structure represents an error raised within the CryptoBackend
 */
public class CryptoBackendException extends RuntimeException {

    /**
     * Initializes a new object.
     *
     * @param message  the detail message
     * @param cause the cause of the exception. A null value is permitted
     */
    public CryptoBackendException(String message, Throwable cause) {
        super(message, cause);
    }
}
