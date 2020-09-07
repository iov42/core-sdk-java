package com.iov42.solutions.core.sdk.model.requests.get;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

/**
 * Request structure for retrieving information of an Identity.
 */
public class GetIdentityRequest extends BaseRequest {

    private final String identityId;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId the identity identifier
     */
    public GetIdentityRequest(String identityId) {
        this.identityId = identityId;
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId  a specific request identifier
     * @param identityId the identity identifier
     */
    public GetIdentityRequest(String requestId, String identityId) {
        super(requestId);
        this.identityId = identityId;
    }

    /**
     * Returns the Identity identifier.
     *
     * @return the Identity identifier
     */
    public String getIdentityId() {
        return identityId;
    }
}
