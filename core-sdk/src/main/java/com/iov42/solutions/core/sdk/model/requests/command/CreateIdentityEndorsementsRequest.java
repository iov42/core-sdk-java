package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.Endorsements;

/**
 * Request structure for endorsing claims of an Identity.
 */
@JsonTypeName("CreateIdentityEndorsementsRequest")
public class CreateIdentityEndorsementsRequest extends CreateEndorsementsRequest {

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId   the Identity identifier
     * @param endorserId   the endorser identifier
     * @param endorsements the {@link Endorsements}
     */
    public CreateIdentityEndorsementsRequest(String identityId, String endorserId, Endorsements endorsements) {
        this(null, identityId, endorserId, endorsements);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId    a specific request identifier
     * @param identityId   the Identity identifier
     * @param endorserId   the endorser identifier
     * @param endorsements the {@link Endorsements}
     */
    public CreateIdentityEndorsementsRequest(String requestId, String identityId, String endorserId, Endorsements endorsements) {
        super(requestId, null, identityId, endorserId, endorsements);
    }
}
