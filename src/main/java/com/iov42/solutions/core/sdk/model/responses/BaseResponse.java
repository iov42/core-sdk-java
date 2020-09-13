package com.iov42.solutions.core.sdk.model.responses;

import java.util.Collection;
import java.util.Map;

public class BaseResponse {

    private byte[] body;

    private Map<String, Collection<String>> headers;

    private String reason;

    private int status;

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
