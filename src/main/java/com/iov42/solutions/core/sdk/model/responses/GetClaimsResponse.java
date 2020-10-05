package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

public class GetClaimsResponse {

    private List<ClaimResponse> claims;

    private String next;

    private GetClaimsResponse() {
        // needed for Jackson parser
    }

    public GetClaimsResponse(List<ClaimResponse> claims, String next) {
        this.claims = claims;
        this.next = next;
    }

    public List<ClaimResponse> getClaims() {
        return claims;
    }

    public String getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "GetClaimsResponse{" +
                "claims=" + claims +
                ", next='" + next + '\'' +
                '}';
    }
}
