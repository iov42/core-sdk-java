package com.iov42.solutions.core.sdk.model;

public class CreateIdentityRequest extends BaseRequest {

    private String identityId;

    private String publicCredentials;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getPublicCredentials() {
        return publicCredentials;
    }

    public void setPublicCredentials(String publicCredentials) {
        this.publicCredentials = publicCredentials;
    }

    @Override
    public String toString() {
        return "CreateIdentityRequest{" +
                "identityId='" + identityId + '\'' +
                ", publicCredentials='" + publicCredentials + '\'' +
                "} " + super.toString();
    }
}
