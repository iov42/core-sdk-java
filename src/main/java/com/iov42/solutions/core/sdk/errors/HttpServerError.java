package com.iov42.solutions.core.sdk.errors;

public class HttpServerError extends Exception {

    private final int code;

    public HttpServerError(String message, int code) {
        super(message);
        this.code = code;
    }

    public HttpServerError(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
