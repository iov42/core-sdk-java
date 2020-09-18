package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class CreateIdentityRequest extends BaseRequest {

    private final String identityId;

    private final PublicCredentials publicCredentials;

    public CreateIdentityRequest(String requestId, String identityId, PublicCredentials credentials) {
        super(requestId);
        this.identityId = identityId;
        this.publicCredentials = credentials;
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
