package com.iov42.solutions.core.sdk.model.requests;

public abstract class BaseRequest {

    private String requestId;

    public BaseRequest() {

    }

    public BaseRequest(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "requestId='" + requestId + '\'' +
                '}';
    }
}
