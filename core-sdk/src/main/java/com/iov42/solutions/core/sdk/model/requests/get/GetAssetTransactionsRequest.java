package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving transaction information of an Asset.
 */
public class GetAssetTransactionsRequest extends GetAssetRequest implements PageableRequest {

    private final Integer limit;

    private final String next;

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     * @param limit       the number of results to be returned (optional)
     * @param next        the first entry to be returned (optional)
     */
    public GetAssetTransactionsRequest(String requestId, String assetTypeId, String assetId, Integer limit, String next) {
        super(requestId, assetTypeId, assetId);
        this.limit = limit;
        this.next = next;
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     */
    public GetAssetTransactionsRequest(String requestId, String assetTypeId, String assetId) {
        this(requestId, assetTypeId, assetId, null, null);
    }

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     * @param limit       the number of results to be returned (optional)
     * @param next        the first entry to be returned (optional)
     */
    public GetAssetTransactionsRequest(String assetTypeId, String assetId, Integer limit, String next) {
        this(null, assetTypeId, assetId, limit, next);
    }

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     */
    public GetAssetTransactionsRequest(String assetTypeId, String assetId) {
        this(null, assetTypeId, assetId, null, null);
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
