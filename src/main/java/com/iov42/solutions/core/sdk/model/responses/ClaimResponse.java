package com.iov42.solutions.core.sdk.model.responses;

public class ClaimResponse extends BaseResponse {

    private String claim;

    private String delegateIdentityId;

    private String resource;

    private ClaimResponse() {
        // needed for Jackson parser
    }

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
