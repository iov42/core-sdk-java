package com.iov42.solutions.core.sdk.model;

import java.util.List;

public class CreateClaimsRequest extends BaseRequest {

    private List<String> claims;

    private String subjectId;

    private String subjectTypeId;

    public List<String> getClaims() {
        return claims;
    }

    public void setClaims(List<String> claims) {
        this.claims = claims;
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
        return "CreateClaimsRequest{" +
                "claims=" + claims +
                ", subjectId='" + subjectId + '\'' +
                ", subjectTypeId='" + subjectTypeId + '\'' +
                "} " + super.toString();
    }
}
