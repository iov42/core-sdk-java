package com.iov42.solutions.core.sdk.model.responses;

/**
 * Structure represents Asset information.
 */
public class AssetInfo {

    private String assetTypeId;

    private String assetId;

    private AssetInfo() {
    }

    AssetInfo(String assetTypeId, String assetId) {
        this.assetTypeId = assetTypeId;
        this.assetId = assetId;
    }

    /**
     * Returns the AssetType identifier.
     *
     * @return the AssetType identifier
     */
    public String getAssetTypeId() {
        return assetTypeId;
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
