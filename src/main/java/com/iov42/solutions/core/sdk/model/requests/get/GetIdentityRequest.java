package com.iov42.solutions.core.sdk.model.requests.get;

public class GetIdentityRequest extends BaseGetRequest {

    private final String identityId;

    public GetIdentityRequest(String nodeId, String identityId) {
        super(nodeId);
        this.identityId = identityId;
    }

    public GetIdentityRequest(String requestId, String nodeId, String identityId) {
        super(requestId, nodeId);
        this.identityId = identityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    @Override
    public String toString() {
        return "GetIdentityRequest{" +
                "identityId='" + identityId + '\'' +
                "} " + super.toString();
    }
}
