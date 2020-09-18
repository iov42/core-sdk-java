package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

import java.util.List;

public class CreateClaimsRequest extends BaseRequest {

    private List<String> claims;

    private String subjectId;

    private String subjectTypeId;

    public CreateClaimsRequest(List<String> claims, String subjectId) {
        super();
        this.claims = claims;
        this.subjectId = subjectId;
    }

    public CreateClaimsRequest(String requestId, List<String> claims, String subjectId) {
        super(requestId);
        this.claims = claims;
        this.subjectId = subjectId;
    }

    public CreateClaimsRequest(List<String> claims, String subjectId, String subjectTypeId) {
        super();
        this.claims = claims;
        this.subjectId = subjectId;
        this.subjectTypeId = subjectTypeId;
    }

    public CreateClaimsRequest(String requestId, List<String> claims, String subjectId, String subjectTypeId) {
        super(requestId);
        this.claims = claims;
        this.subjectId = subjectId;
        this.subjectTypeId = subjectTypeId;
    }

    public List<String> getClaims() {
        return claims;
    }

    public void setClaims(List<String> claims) {
        this.claims = claims;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectTypeId() {
        return subjectTypeId;
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
