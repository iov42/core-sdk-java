package com.iov42.solutions.core.sdk.model.requests;

import com.iov42.solutions.core.sdk.model.PublicCredentials;

import java.util.UUID;

public class CreateIdentityRequest extends AuthorisedRequest {

    private final String identityId;

    private final PublicCredentials publicCredentials;

    public CreateIdentityRequest(PublicCredentials publicCredentials) {
        super(UUID.randomUUID().toString());
        this.identityId = UUID.randomUUID().toString();
        this.publicCredentials = publicCredentials;
    }

    public CreateIdentityRequest(String identityId, PublicCredentials publicCredentials) {
        super(UUID.randomUUID().toString());
        this.identityId = identityId;
        this.publicCredentials = publicCredentials;
    }

    public CreateIdentityRequest(String requestId, String identityId, PublicCredentials publicCredentials) {
        super(requestId);
        this.identityId = identityId;
        this.publicCredentials = publicCredentials;
    }

    public String getIdentityId() {
        return identityId;
    }

    public PublicCredentials getPublicCredentials() {
        return publicCredentials;
    }

    @Override
    public String toString() {
        return "CreateIdentityRequest{" +
                "identityId='" + identityId + '\'' +
                ", publicCredentials='" + publicCredentials + '\'' +
                "} " + super.toString();
    }
}
