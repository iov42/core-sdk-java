package com.iov42.solutions.core.sdk.model.requests.put;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Data structure used to create claims PUT request.
 */
public abstract class CreateClaimsRequest extends BasePutRequest {

    /**
     * The identifier to which the claims will be linked.
     */
    private final String subjectId;

    /**
     * A non-empty list of all the plain text claims.
     */
    private List<String> claims;

    /**
     * The identifier of the asset-type of the asset against which the claims will be linked.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subjectTypeId;

    public CreateClaimsRequest(TransactionType _type, String subjectId, String subjectTypeId, List<String> claims) {
        super(_type);
        this.subjectId = subjectId;
        this.subjectTypeId = subjectTypeId;
        this.claims = claims;
    }

    public CreateClaimsRequest(TransactionType _type, String subjectId, List<String> claims) {
        super(_type);
        this.subjectId = subjectId;
        this.claims = claims;
    }

    public CreateClaimsRequest(String requestId, TransactionType _type, String subjectId, List<String> claims) {
        super(requestId, _type);
        this.subjectId = subjectId;
        this.claims = claims;
    }

    public CreateClaimsRequest(String requestId, TransactionType _type, String subjectId, String subjectTypeId, List<String> claims) {
        super(requestId, _type);
        this.subjectId = subjectId;
        this.subjectTypeId = subjectTypeId;
        this.claims = claims;
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
}
