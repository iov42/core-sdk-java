package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

public class ClaimEndorsementsResponse extends BaseResponse {

    private String claim;

    private String delegateIdentityId;

    private List<EndorsementResponse> endorsements;

    public ClaimEndorsementsResponse(String proof, String claim, String delegateIdentityId, List<EndorsementResponse> endorsements) {
        super(proof);
        this.claim = claim;
        this.delegateIdentityId = delegateIdentityId;
        this.endorsements = endorsements;
    }

    public String getClaim() {
        return claim;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public List<EndorsementResponse> getEndorsements() {
        return endorsements;
    }

    @Override
    public String toString() {
        return "ClaimEndorsementsResponse{" +
                "claim='" + claim + '\'' +
                ", delegateIdentityId='" + delegateIdentityId + '\'' +
                ", endorsements=" + endorsements +
                "} " + super.toString();
    }
}
