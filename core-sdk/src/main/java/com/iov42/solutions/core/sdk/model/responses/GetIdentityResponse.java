package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.PublicCredentials;

import java.util.List;

/**
 * Structure represents identity information.
 */
public class GetIdentityResponse extends BaseResponse {

    private String identityId;

    private List<PublicCredentials> publicCredentials;

    private GetIdentityResponse() {}

    GetIdentityResponse(String proof, String identityId, List<PublicCredentials> publicCredentials) {
        super(proof);
        this.identityId = identityId;
        this.publicCredentials = publicCredentials;
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
     * Returns the {@link PublicCredentials} of an Identity.
     *
     * @return the {@link PublicCredentials} of an Identity
     */
    public List<PublicCredentials> getPublicCredentials() {
        return publicCredentials;
    }
}
