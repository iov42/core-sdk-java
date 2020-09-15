package com.iov42.solutions.core.sdk.errors;

import java.util.List;

public class PlatformError {

    private List<BasePlatformError> errors;

    private String requestId;

    public List<BasePlatformError> getErrors() {
        return errors;
    }

    public void setErrors(List<BasePlatformError> errors) {
        this.errors = errors;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "{ errors=" + errors +
                ", requestId='" + requestId + " }";
    }
}
