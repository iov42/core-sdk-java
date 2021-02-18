package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iov42.solutions.core.sdk.model.Claims;

/**
 * Wraps functionality for requests that require claims to be created.
 */
interface ClaimsContainerRequest {

    /**
     * Returns whether a request is an endorsement.
     *
     * @return {@code true} if it is an endorsement, {@code false} otherwise.
     */
    @JsonIgnore
    boolean isEndorsement();

    /**
     * Returns the contained claims.
     *
     * @return the contained claims.
     */
    Claims getClaims();
}
