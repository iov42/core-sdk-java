package com.iov42.solutions.core.sdk.model.requests.put;

import com.iov42.solutions.core.sdk.model.PublicCredentials;

/**
 * Data structure used to create new identity.
 */
public class CreateIdentityRequest extends BasePutRequest {

    /**
     * The identity identifier for the identity being created.
     */
    private final String identityId;

    /**
     * Publicly available details about a single key, public key and protocol used to generate the public/private pair, used for signing and verification.
     */
    private final PublicCredentials publicCredentials;

    public CreateIdentityRequest(String identityId, PublicCredentials credentials) {
        super(TransactionType.IssueIdentityRequest);
        this.identityId = identityId;
        this.publicCredentials = credentials;
    }

    public CreateIdentityRequest(String requestId, String identityId, PublicCredentials credentials) {
        super(requestId, TransactionType.IssueIdentityRequest);
        this.identityId = identityId;
        this.publicCredentials = credentials;
    }

    public String getIdentityId() {
        return identityId;
    }

    public PublicCredentials getPublicCredentials() {
        return publicCredentials;
    }
}
