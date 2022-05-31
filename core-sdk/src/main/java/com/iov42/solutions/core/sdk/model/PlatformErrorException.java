package com.iov42.solutions.core.sdk.model;

import com.iov42.solutions.core.sdk.model.responses.ErrorResponse;

/**
 * Structure represents an error reported by the iov42 platform.
 */
public class PlatformErrorException extends RuntimeException {

    private final int statusCode;

    private final transient ErrorResponse errorResponse;

    /**
     * Initializes a new object.
     *
     * @param statusCode    the http status code from the error response
     * @param errorResponse the {@link ErrorResponse} from the iov42 platform
     */
    public PlatformErrorException(int statusCode, ErrorResponse errorResponse) {
        super(getDetailMessage(statusCode, errorResponse));
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

    private static String getDetailMessage(int statusCode, ErrorResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Status code: ").append(statusCode).append("\n");
        for (PlatformError error : response.getErrors()) {
            sb.append("* code: ").append(error.getErrorCode())
                    .append(", type: ").append(error.getErrorType())
                    .append(", '").append(error.getMessage()).append("'\n");
        }
        return sb.toString();
    }
}
