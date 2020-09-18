package com.iov42.solutions.core.sdk.model.requests.get;

public class GetIdentityClaimsRequest extends GetIdentityRequest {

    private int limit;

    private String next;

    public GetIdentityClaimsRequest(String nodeId, String identityId) {
        super(nodeId, identityId);
    }

    public GetIdentityClaimsRequest(String requestId, String nodeId, String identityId) {
        super(requestId, nodeId, identityId);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "GetIdentityClaimsRequest{" +
                "limit=" + limit +
                ", next='" + next + '\'' +
                "} " + super.toString();
    }
}
