package com.iov42.solutions.core.sdk.model.requests;

import java.util.UUID;

/**
 * Base class for requests.
 */
public abstract class BaseRequest {

    private final String requestId;

    protected BaseRequest() {
        this(null);
    }

    protected BaseRequest(String requestId) {
        this.requestId = requestId != null
                ? requestId
                : UUID.randomUUID().toString();
    }

    /**
     * Returns the request id.
     *
     * @return the request id
     */
    public String getRequestId() {
        return requestId;
    }
}
