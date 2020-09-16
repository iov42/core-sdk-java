package com.iov42.solutions.core.sdk.model.responses;

public class ClaimResponse {

    private String claim;

    private String delegateIdentityId;

    private String proof;

    private String resource;

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public void setDelegateIdentityId(String delegateIdentityId) {
        this.delegateIdentityId = delegateIdentityId;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "ClaimResponse{" +
                "claim='" + claim + '\'' +
                ", delegateIdentityId='" + delegateIdentityId + '\'' +
                ", proof='" + proof + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }
}
