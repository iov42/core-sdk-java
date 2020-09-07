package com.iov42.solutions.core.sdk.http;


/**
 * Wrapper for {@link HttpBackend} exceptions.
 */
public class HttpBackendException extends RuntimeException {

    /**
     * Initializes a new exception instance.
     *
     * @param message the exception message
     */
    public HttpBackendException(String message) {
        super(message);
    }

    /**
     * Initializes a new exception instance.
     *
     * @param message the exception message
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public HttpBackendException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes a new exception instance.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public HttpBackendException(Throwable cause) {
        super(cause);
    }
}
