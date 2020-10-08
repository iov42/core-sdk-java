package com.iov42.solutions.core.sdk.model.requests;

import java.util.UUID;

public abstract class BaseRequest {

    /**
     * Unique identifier associated with the request.
     */
    private final String requestId;

    protected BaseRequest() {
        this.requestId = UUID.randomUUID().toString();
    }

    protected BaseRequest(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}
