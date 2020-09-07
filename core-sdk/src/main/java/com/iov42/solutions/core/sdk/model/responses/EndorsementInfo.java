package com.iov42.solutions.core.sdk.model.responses;

/**
 * Structure represents information of an Endorsement.
 */
public class EndorsementInfo {

    private String delegateIdentityId;

    private String endorsement;

    private String endorserId;

    private String proof;

    private EndorsementInfo() {
    }

    EndorsementInfo(String delegateIdentityId, String endorsement, String endorserId, String proof) {
        this.delegateIdentityId = delegateIdentityId;
        this.endorsement = endorsement;
        this.endorserId = endorserId;
        this.proof = proof;
    }

    /**
     * Returns the delegate Identity identifier of the claim creator or {@code null}.
     *
     * @return the delegate Identity identifier of the claim creator or {@code null}
     */
    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public String getEndorsement() {
        return endorsement;
    }

    /**
     * Returns the endorser's identity identifier.
     *
     * @return the endorser's identity identifier
     */
    public String getEndorserId() {
        return endorserId;
    }

    /**
     * Returns a link to the proof for the endorsement.
     *
     * @return a link to the proof for the endorsement
     */
    public String getProof() {
        return proof;
    }
}
