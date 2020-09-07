package com.iov42.solutions.core.sdk.model;

import com.iov42.solutions.core.sdk.model.responses.ErrorResponse;

/**
 * Structure represents an error reported by the iov42 platform.
 */
public class PlatformErrorException extends RuntimeException {

    private final int statusCode;

    private final ErrorResponse errorResponse;

    /**
     * Initializes a new object.
     *
     * @param statusCode    the http status code from the error response
     * @param errorResponse the {@link ErrorResponse} from the iov42 platform
     */
    public PlatformErrorException(int statusCode, ErrorResponse errorResponse) {
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }

    /**
     * Returns the {@link ErrorResponse} from the iov42 platform or {@code null}.
     *
     * @return the {@link ErrorResponse} from the iov42 platform or {@code null}
     */
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    /**
     * Returns the http status code from the error response.
     *
     * @return the http status code from the error response
     */
    public int getStatusCode() {
        return statusCode;
    }
}
