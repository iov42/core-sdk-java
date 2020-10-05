package com.iov42.solutions.core.sdk.model.responses;

public class EndorsementResponse extends BaseResponse {

    private String delegateIdentityId;

    private String endorsement;

    private String endorserId;

    private EndorsementResponse() {
        // needed for Jackson parser
    }

    public EndorsementResponse(String delegateIdentityId, String endorsement, String endorserId, String proof) {
        super(proof);
        this.delegateIdentityId = delegateIdentityId;
        this.endorsement = endorsement;
        this.endorserId = endorserId;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public String getEndorsement() {
        return endorsement;
    }

    public String getEndorserId() {
        return endorserId;
    }

    @Override
    public String toString() {
        return "EndorsementResponse{" +
                "delegateIdentityId='" + delegateIdentityId + '\'' +
                ", endorsement='" + endorsement + '\'' +
                ", endorserId='" + endorserId + '\'' +
                "} " + super.toString();
    }
}
