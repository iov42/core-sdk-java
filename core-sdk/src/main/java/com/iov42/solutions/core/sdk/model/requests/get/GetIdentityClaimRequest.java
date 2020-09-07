package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving information of a claim of an Identity.
 */
public class GetIdentityClaimRequest extends GetIdentityRequest {

    private final String hashedClaim;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId  the Identity identifier
     * @param hashedClaim the hashed claim to be queried
     */
    public GetIdentityClaimRequest(String identityId, String hashedClaim) {
        this(null, identityId, hashedClaim);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param identityId  the Identity identifier
     * @param hashedClaim the hashed claim to be queried
     */
    public GetIdentityClaimRequest(String requestId, String identityId, String hashedClaim) {
        super(requestId, identityId);
        this.hashedClaim = hashedClaim;
    }

    /**
     * Returns the hashed claim.
     *
     * @return the hashed claim
     */
    public String getHashedClaim() {
        return hashedClaim;
    }
}
