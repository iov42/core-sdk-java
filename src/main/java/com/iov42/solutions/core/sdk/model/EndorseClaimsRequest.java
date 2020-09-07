package com.iov42.solutions.core.sdk.model;

import java.util.List;

public class EndorseClaimsRequest extends BaseRequest {

    private List<String> endorsements;

    private String endorserId;

    private String subjectId;

    private String subjectTypeId;

    public List<String> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(List<String> endorsements) {
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

    public String getSubjectTypeId() {
        return subjectTypeId;
    }

    public void setSubjectTypeId(String subjectTypeId) {
        this.subjectTypeId = subjectTypeId;
    }

    @Override
    public String toString() {
        return "EndorseClaimsRequest{" +
                "endorsements=" + endorsements +
                ", endorserId='" + endorserId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectTypeId='" + subjectTypeId + '\'' +
                "} " + super.toString();
    }
}
