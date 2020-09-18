package com.iov42.solutions.core.sdk.model.requests;

import java.util.Map;

public class CreateEndorsementsRequest extends BaseRequest {

    private Map<String, String> endorsements;

    private String endorserId;

    private String subjectId;

    public CreateEndorsementsRequest() {
    }

    public CreateEndorsementsRequest(String requestId, String subjectId, String endorserId, Map<String, String> endorsements) {
        super(requestId);
        this.subjectId = subjectId;
        this.endorserId = endorserId;
        this.endorsements = endorsements;
    }

    public Map<String, String> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(Map<String, String> endorsements) {
        this.endorsements = endorsements;
    }

    public String getEndorserId() {
        return endorserId;
    }

    public void setEndorserId(String endorserId) {
        this.endorserId = endorserId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "CreateEndorsementsRequest{" +
                "endorsements=" + endorsements +
                ", endorserId='" + endorserId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                "} " + super.toString();
    }
}
