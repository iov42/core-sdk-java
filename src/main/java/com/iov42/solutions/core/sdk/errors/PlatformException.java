package com.iov42.solutions.core.sdk.errors;

public class PlatformException extends Exception {

    public PlatformException() {
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlatformException(Throwable cause) {
        super(cause);
    }
}
