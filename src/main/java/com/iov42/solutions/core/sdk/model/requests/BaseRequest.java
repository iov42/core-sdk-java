package com.iov42.solutions.core.sdk.model.requests;

import java.util.UUID;

public abstract class BaseRequest {

    private final String requestId;

    public BaseRequest() {
        this.requestId = UUID.randomUUID().toString();
    }

    public BaseRequest(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "requestId='" + requestId + '\'' +
                '}';
    }
}
