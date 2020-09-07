package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving Asset information.
 */
public class GetAssetRequest extends GetAssetTypeRequest {

    private final String assetId;

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId an AssetType identifier
     * @param assetId     a Asset identifier
     */
    public GetAssetRequest(String requestId, String assetTypeId, String assetId) {
        super(requestId, assetTypeId);
        this.assetId = assetId;
    }

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId an AssetType identifier
     * @param assetId     a Asset identifier
     */
    public GetAssetRequest(String assetTypeId, String assetId) {
        this(null, assetTypeId, assetId);
    }

    /**
     * Returns the Asset identifier.
     *
     * @return the Asset identifier
     */
    public String getAssetId() {
        return assetId;
    }
}
