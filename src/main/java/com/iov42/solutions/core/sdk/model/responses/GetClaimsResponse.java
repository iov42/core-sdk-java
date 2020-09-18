package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

public class GetClaimsResponse {

    private final List<ClaimResponse> claims;

    private final String next;

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
