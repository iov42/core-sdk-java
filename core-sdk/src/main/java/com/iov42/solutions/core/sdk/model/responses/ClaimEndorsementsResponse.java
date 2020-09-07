package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

/**
 * Structure represents information of endorsements of a claim.
 */
public class ClaimEndorsementsResponse extends BaseResponse {

    private String claim;

    private String delegateIdentityId;

    private List<EndorsementInfo> endorsements;

    private ClaimEndorsementsResponse() {
        // needed for Jackson parser
    }

    ClaimEndorsementsResponse(String proof, String claim, String delegateIdentityId, List<EndorsementInfo> endorsements) {
        super(proof);
        this.claim = claim;
        this.delegateIdentityId = delegateIdentityId;
        this.endorsements = endorsements;
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
     * Returns a {@link List} of endorsements.
     *
     * @return a {@link List} of endorsements
     */
    public List<EndorsementInfo> getEndorsements() {
        return endorsements;
    }
}
