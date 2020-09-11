package com.iov42.solutions.core.sdk.model.requests;

import com.google.gson.annotations.Expose;

public abstract class BaseRequest {

    @Expose
    private String requestId;

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
