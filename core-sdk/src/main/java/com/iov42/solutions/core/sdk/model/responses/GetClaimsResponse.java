package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

/**
 * Structure represents the response of a claims information request.
 */
public class GetClaimsResponse {

    private List<ClaimInfo> claims;

    private String next;

    private GetClaimsResponse() {}

    GetClaimsResponse(List<ClaimInfo> claims, String next) {
        this.claims = claims;
        this.next = next;
    }

    /**
     * Returns a {@link List} of {@link ClaimInfo} instances.
     *
     * @return a {@link List} of {@link ClaimInfo} instances
     */
    public List<ClaimInfo> getClaims() {
        return claims;
    }

    /**
     * Returns the entry that the next page will start from.
     * If {@code null}, it means that this was the last page.
     *
     * @return the entry that the next page will start from
     */
    public String getNext() {
        return next;
    }
}
