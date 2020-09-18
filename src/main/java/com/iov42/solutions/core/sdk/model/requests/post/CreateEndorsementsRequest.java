package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

import java.util.Map;

public class CreateEndorsementsRequest extends BaseRequest {

    private Map<String, String> endorsements;

    private String endorserId;

    private String subjectId;

    public CreateEndorsementsRequest(String subjectId, String endorserId, Map<String, String> endorsements) {
        super();
        this.subjectId = subjectId;
        this.endorserId = endorserId;
        this.endorsements = endorsements;
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

    public String getEndorserId() {
        return endorserId;
    }

    public String getSubjectId() {
        return subjectId;
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
