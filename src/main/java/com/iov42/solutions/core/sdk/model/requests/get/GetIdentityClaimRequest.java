package com.iov42.solutions.core.sdk.model.requests.get;

public class GetIdentityClaimRequest extends GetIdentityRequest {

    private String hashedClaim;

    public GetIdentityClaimRequest(String nodeId, String identityId, String hashedClaim) {
        super(nodeId, identityId);
        this.hashedClaim = hashedClaim;
    }

    public GetIdentityClaimRequest(String requestId, String nodeId, String identityId, String hashedClaim) {
        super(requestId, nodeId, identityId);
        this.hashedClaim = hashedClaim;
    }

    public String getHashedClaim() {
        return hashedClaim;
    }

    @Override
    public String toString() {
        return "GetIdentityClaimRequest{" +
                "hashedClaim='" + hashedClaim + '\'' +
                "} " + super.toString();
    }
}
