package com.iov42.solutions.core.sdk.model.requests.get;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

/**
 * Request structure for retrieving AssetType information.
 */
public class GetAssetTypeRequest extends BaseRequest {

    private final String assetTypeId;

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId an AssetType identifier
     */
    public GetAssetTypeRequest(String assetTypeId) {
        this(null, assetTypeId);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId an AssetType identifier
     */
    public GetAssetTypeRequest(String requestId, String assetTypeId) {
        super(requestId);
        this.assetTypeId = assetTypeId;
    }

    /**
     * Returns the AssetType identifier.
     *
     * @return the AssetType identifier
     */
    public String getAssetTypeId() {
        return assetTypeId;
    }
}
