package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving Asset claims information.
 */
public class GetAssetClaimsRequest extends GetAssetRequest implements PageableRequest {

    private final Integer limit;

    private final String next;

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId an AssetType identifier
     * @param assetId     a Asset identifier
     */
    public GetAssetClaimsRequest(String assetTypeId, String assetId) {
        this(null, assetTypeId, assetId, null, null);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId an AssetType identifier
     * @param assetId     a Asset identifier
     * @param limit       the number of results to be returned (optional)
     * @param next        the first entry to be returned (optional)
     */
    public GetAssetClaimsRequest(String requestId, String assetTypeId, String assetId, Integer limit, String next) {
        super(requestId, assetTypeId, assetId);
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
