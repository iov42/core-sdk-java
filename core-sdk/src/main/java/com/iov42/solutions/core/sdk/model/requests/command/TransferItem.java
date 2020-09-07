package com.iov42.solutions.core.sdk.model.requests.command;

/**
 * Base structure for transfer items (to be used with the {@link TransfersRequest}).
 */
public abstract class TransferItem {

    private final String assetTypeId;

    protected TransferItem(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    /**
     * Returns the AssetType identifier.
     *
     * @return the AssetType identifier.
     */
    public String getAssetTypeId() {
        return assetTypeId;
    }
}
