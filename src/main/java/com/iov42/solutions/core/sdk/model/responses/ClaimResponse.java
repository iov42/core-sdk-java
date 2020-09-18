package com.iov42.solutions.core.sdk.model.responses;

public class ClaimResponse extends BaseResponse {

    private final String claim;

    private final String delegateIdentityId;

    private final String resource;

    public ClaimResponse(String claim, String delegateIdentityId, String proof, String resource) {
        super(proof);
        this.claim = claim;
        this.delegateIdentityId = delegateIdentityId;
        this.resource = resource;
    }

    public String getClaim() {
        return claim;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return "ClaimResponse{" +
                "claim='" + claim + '\'' +
                ", delegateIdentityId='" + delegateIdentityId + '\'' +
                ", resource='" + resource + '\'' +
                "} " + super.toString();
    }
}
