package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving information of claims of an Identity.
 */
public class GetIdentityClaimsRequest extends GetIdentityRequest implements PageableRequest {

    private final Integer limit;

    private final String next;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId the Identity identifier
     */
    public GetIdentityClaimsRequest(String identityId) {
        this(null, identityId);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId  a specific request identifier
     * @param identityId the Identity identifier
     */
    public GetIdentityClaimsRequest(String requestId, String identityId) {
        this(requestId, identityId, null, null);
    }

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param identityId the Identity identifier
     * @param limit      the number of results to be returned (optional)
     * @param next       the first entry to be returned (optional)
     */
    public GetIdentityClaimsRequest(String identityId, Integer limit, String next) {
        this(null, identityId, limit, next);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId  a specific request identifier
     * @param identityId the Identity identifier
     * @param limit      the number of results to be returned (optional)
     * @param next       the first entry to be returned (optional)
     */
    public GetIdentityClaimsRequest(String requestId, String identityId, Integer limit, String next) {
        super(requestId, identityId);
        this.limit = limit;
        this.next = next;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getLimit() {
        return limit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNext() {
        return next;
    }
}
