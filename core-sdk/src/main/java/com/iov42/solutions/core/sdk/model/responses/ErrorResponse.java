package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.PlatformError;

import java.util.List;

/**
 * Response structure represents an error reported from the iov42 platform.
 */
public class ErrorResponse {

    private String requestId;

    private String proof;

    private List<PlatformError> errors;

    private ErrorResponse() {
    }

    ErrorResponse(String requestId, String proof, List<PlatformError> errors) {
        this.requestId = requestId;
        this.proof = proof;
        this.errors = errors;
    }

    /**
     * Returns the request identifier used in the erroneous request.
     *
     * @return the request identifier used in the erroneous request
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns a link to the proof of the erroneous request.
     *
     * @return a link to the proof of the erroneous request
     */
    public String getProof() {
        return proof;
    }

    /**
     * Returns a {@link List} of {@link PlatformError} items.
     *
     * @return a {@link List} of {@link PlatformError} items
     */
    public List<PlatformError> getErrors() {
        return errors;
    }
}
