package com.iov42.solutions.core.sdk.errors;

public class HttpClientError extends Exception {

    private final int code;

    public HttpClientError(String message, int code) {
        super(message);
        this.code = code;
    }

    public HttpClientError(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
