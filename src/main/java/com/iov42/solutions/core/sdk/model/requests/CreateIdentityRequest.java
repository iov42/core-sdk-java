package com.iov42.solutions.core.sdk.model.requests;

import com.iov42.solutions.core.sdk.model.PublicCredentials;

public class CreateIdentityRequest extends AuthorisedRequest {

    private final String identityId;

    private final PublicCredentials publicCredentials;

    public CreateIdentityRequest(String identityId, PublicCredentials publicCredentials) {
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
