package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.Claims;

/**
 * Request structure for creating claims for an Identity.
 */
@JsonTypeName("CreateIdentityClaimsRequest")
public class CreateIdentityClaimsRequest extends CreateClaimsRequest {

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId the Identity identifier
     * @param claims     the {@link Claims}
     */
    public CreateIdentityClaimsRequest(String identityId, Claims claims) {
        this(null, identityId, claims);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId  a specific request identifier
     * @param identityId the Identity identifier
     * @param claims     the {@link Claims}
     */
    public CreateIdentityClaimsRequest(String requestId, String identityId, Claims claims) {
        super(requestId, null, identityId, claims);
    }
}
