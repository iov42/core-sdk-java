package com.iov42.solutions.core.sdk.model.requests;

public class GetIdentityRequest extends BaseRequest {

    private String identityId;

    private String nodeId;

    public GetIdentityRequest() {
    }

    public GetIdentityRequest(String requestId, String identityId, String nodeId) {
        super(requestId);
        this.identityId = identityId;
        this.nodeId = nodeId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return "GetIdentityRequest{" +
                "identityId='" + identityId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                "} " + super.toString();
    }
}
