package com.iov42.solutions.core.sdk.model;

/**
 * Structure represents an error from the iov42 platform.
 */
public class PlatformError {

    private int errorCode;

    private String errorType;

    private String message;

    private PlatformError() {
    }

    /**
     * Initializes a new error object.
     *
     * @param errorCode the error code
     * @param errorType the error type
     * @param message   the error message
     */
    public PlatformError(int errorCode, String errorType, String message) {
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.message = message;
    }

    /**
     * Returns the error code.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the error type.
     *
     * @return the error type
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Returns the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }
}
