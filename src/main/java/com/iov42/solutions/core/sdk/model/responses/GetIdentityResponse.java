package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.GetIdentityRequest;

import java.util.List;

/**
 * Response object of the {@link com.iov42.solutions.core.sdk.PlatformClient#getIdentity(GetIdentityRequest, IovKeyPair)}
 */
public class GetIdentityResponse extends BaseResponse {

    private final String identityId;

    private final List<PublicCredentials> publicCredentials;

    public GetIdentityResponse(String proof, String identityId, List<PublicCredentials> publicCredentials) {
        super(proof);
        this.identityId = identityId;
        this.publicCredentials = publicCredentials;
    }

    public String getIdentityId() {
        return identityId;
    }

    public List<PublicCredentials> getPublicCredentials() {
        return publicCredentials;
    }

    @Override
    public String toString() {
        return "GetIdentityResponse{" +
                "identityId='" + identityId + '\'' +
                ", publicCredentials=" + publicCredentials +
                "} " + super.toString();
    }
}
