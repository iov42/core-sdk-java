package com.iov42.solutions.core.sdk.model.requests;

public class GetIdentityClaimsRequest extends GetIdentityRequest {

    private int limit;

    private String next;

    public GetIdentityClaimsRequest() {
    }

    public GetIdentityClaimsRequest(String requestId, String identityId, String nodeId, String next, int limit) {
        super(requestId, identityId, nodeId);
        this.next = next;
        this.limit = limit;
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

}
