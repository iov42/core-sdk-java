package com.iov42.solutions.core.sdk.model.requests;

public class GetIdentityClaimRequest extends GetIdentityRequest {

    private String hashedClaim;

    public GetIdentityClaimRequest() {
    }

    public GetIdentityClaimRequest(String requestId, String identityId, String nodeId, String hashedClaim) {
        super(requestId, identityId, nodeId);
        this.hashedClaim = hashedClaim;
    }

    public String getHashedClaim() {
        return hashedClaim;
    }

    public void setHashedClaim(String hashedClaim) {
        this.hashedClaim = hashedClaim;
    }

    @Override
    public String toString() {
        return "GetIdentityClaimRequest{" +
                "hashedClaim='" + hashedClaim + '\'' +
                "} " + super.toString();
    }
}
