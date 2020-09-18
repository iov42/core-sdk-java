package com.iov42.solutions.core.sdk.model.requests.get;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class BaseGetRequest extends BaseRequest {

    private final String nodeId;

    public BaseGetRequest(String nodeId) {
        super();
        this.nodeId = nodeId;
    }

    public BaseGetRequest(String requestId, String nodeId) {
        super(requestId);
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return "BaseGetRequest{" +
                "nodeId='" + nodeId + '\'' +
                "} " + super.toString();
    }
}
