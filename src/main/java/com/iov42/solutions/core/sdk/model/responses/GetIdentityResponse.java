package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.KeyInfo;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityRequest;

import java.util.List;

/**
 * Response object of the {@link com.iov42.solutions.core.sdk.PlatformClient#getIdentity(GetIdentityRequest, KeyInfo)}
 */
public class GetIdentityResponse extends BaseResponse {

    private String identityId;

    private List<PublicCredentials> publicCredentials;

    private GetIdentityResponse() {
        // needed for Jackson parser
    }

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
