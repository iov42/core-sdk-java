package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.PublicCredentials;

/**
 * Request structure for creating an Identity.
 */
@JsonTypeName("IssueIdentityRequest")
public class CreateIdentityRequest extends BaseCommandRequest {

    private final String identityId;

    private final PublicCredentials publicCredentials;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId  the Identity identifier
     * @param credentials the {@link PublicCredentials}
     */
    public CreateIdentityRequest(String identityId, PublicCredentials credentials) {
        this(null, identityId, credentials);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param identityId  the Identity identifier
     * @param credentials the {@link PublicCredentials}
     */
    public CreateIdentityRequest(String requestId, String identityId, PublicCredentials credentials) {
        super(requestId);
        this.identityId = identityId;
        this.publicCredentials = credentials;
    }

    /**
     * Returns the Identity identifier.
     *
     * @return the Identity identifier
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * Returns the {@link PublicCredentials} of the Identity.
     *
     * @return the {@link PublicCredentials}
     */
    public PublicCredentials getPublicCredentials() {
        return publicCredentials;
    }
}
