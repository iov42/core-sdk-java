package com.iov42.solutions.core.sdk.model.responses;

/**
 * Structure information of a Claim.
 */
public class ClaimInfo {

    private String claim;

    private String delegateIdentityId;

    private String proof;

    private String resource;

    private ClaimInfo() {
    }

    ClaimInfo(String claim, String delegateIdentityId, String proof, String resource) {
        this.claim = claim;
        this.delegateIdentityId = delegateIdentityId;
        this.proof = proof;
        this.resource = resource;
    }

    /**
     * Returns the (hashed) claim.
     *
     * @return the (hashed) claim
     */
    public String getClaim() {
        return claim;
    }

    /**
     * Returns the delegate Identity identifier of the claim creator or {@code null}.
     *
     * @return the delegate Identity identifier of the claim creator or {@code null}
     */
    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    /**
     * Returns a link the the claim resource.
     *
     * @return a link the the claim resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns a link to the proof for this claim.
     *
     * @return a link to the proof for this claim
     */
    public String getProof() {
        return proof;
    }
}
