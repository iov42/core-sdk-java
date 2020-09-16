package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

public class GetClaimsResponse {

    private List<ClaimResponse> claims;

    private String next;

    public List<ClaimResponse> getClaims() {
        return claims;
    }

    public void setClaims(List<ClaimResponse> claims) {
        this.claims = claims;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "GetClaimsResponse{" +
                "claims=" + claims +
                ", next='" + next + '\'' +
                '}';
    }
}
