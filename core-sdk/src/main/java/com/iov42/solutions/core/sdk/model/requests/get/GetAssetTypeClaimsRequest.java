package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving AssetType claims information.
 */
public class GetAssetTypeClaimsRequest extends GetAssetTypeRequest implements PageableRequest {

    private final Integer limit;

    private final String next;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     */
    public GetAssetTypeClaimsRequest(String assetTypeId) {
        this(null, assetTypeId);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     */
    public GetAssetTypeClaimsRequest(String requestId, String assetTypeId) {
        this(requestId, assetTypeId, null, null);
    }

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param limit       the number of results to be returned (optional)
     * @param next        the first entry to be returned (optional)
     */
    public GetAssetTypeClaimsRequest(String assetTypeId, Integer limit, String next) {
        this(null, assetTypeId, limit, next);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param limit       the number of results to be returned (optional)
     * @param next        the first entry to be returned (optional)
     */
    public GetAssetTypeClaimsRequest(String requestId, String assetTypeId, Integer limit, String next) {
        super(requestId, assetTypeId);
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
