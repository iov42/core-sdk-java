package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.GetIdentityRequest;

import java.util.List;

/**
 * Response object of the {@link com.iov42.solutions.core.sdk.PlatformClient#getIdentity(GetIdentityRequest, IovKeyPair)}
 */
public class GetIdentityResponse extends BaseResponse {

    private String identityId;

    private List<PublicCredentials> publicCredentials;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public List<PublicCredentials> getPublicCredentials() {
        return publicCredentials;
    }

    public void setPublicCredentials(List<PublicCredentials> publicCredentials) {
        this.publicCredentials = publicCredentials;
    }

    @Override
    public String toString() {
        return "GetIdentityResponse{" +
                "identityId='" + identityId + '\'' +
                ", publicCredentials=" + publicCredentials +
                "} " + super.toString();
    }
}
