package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.errors.PlatformError;

import java.util.List;

public abstract class BaseResponse {

    /**
     * The error messages with type and code will be populated in case of HTTP code >=400.
     */
    private List<PlatformError> errors;

    /**
     * The location of the generated proof for this request.
     */
    private String proof;

    /**
     * Unique identifier associated with the request.
     */
    private String requestId;

    /**
     * The locations of the affected resources.
     */
    private List<String> resources;

    public BaseResponse() {
        // needed for Jackson parser
    }

    public BaseResponse(String proof) {
        this.proof = proof;
    }

    public List<PlatformError> getErrors() {
        return errors;
    }

    public void setErrors(List<PlatformError> errors) {
        this.errors = errors;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public boolean hasFinished() {
        return resources != null;
    }
}
