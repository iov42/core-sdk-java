package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Request structure for adding a delegate to an identity.
 */
@JsonTypeName("AddDelegateRequest")
public class AddDelegateRequest extends BaseCommandRequest {

    private final String delegateIdentityId;

    private final String delegatorIdentityId;

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param delegateIdentityId  the Identity identifier of the delegate
     * @param delegatorIdentityId the Identity identifier of the delegator
     */
    public AddDelegateRequest(String delegateIdentityId, String delegatorIdentityId) {
        this(null, delegateIdentityId, delegatorIdentityId);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId           a specific request identifier
     * @param delegateIdentityId  the Identity identifier of the delegate
     * @param delegatorIdentityId the Identity identifier of the delegator
     */
    public AddDelegateRequest(String requestId, String delegateIdentityId, String delegatorIdentityId) {
        super(requestId);
        this.delegateIdentityId = delegateIdentityId;
        this.delegatorIdentityId = delegatorIdentityId;
    }

    /**
     * Returns the delegate Identity identifier.
     *
     * @return the delegate Identity identifier.
     */
    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    /**
     * Returns the delegator Identity identifier.
     *
     * @return the delegator Identity identifier.
     */
    public String getDelegatorIdentityId() {
        return delegatorIdentityId;
    }
}
