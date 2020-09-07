package com.iov42.solutions.core.sdk.model.responses;

import java.math.BigInteger;

/**
 * Represents the response structure for the GetAsset request.
 */
public class GetAssetResponse extends BaseResponse {

    private String assetId;

    private String assetTypeId;

    private String ownerId;

    private BigInteger quantity;

    private GetAssetResponse() {
    }

    GetAssetResponse(String proof, String assetId, String assetTypeId, String ownerId, BigInteger quantity) {
        super(proof);
        this.assetId = assetId;
        this.assetTypeId = assetTypeId;
        this.ownerId = ownerId;
        this.quantity = quantity;
    }

    /**
     * Returns the Asset identifier.
     *
     * @return the Asset identifier
     */
    public String getAssetId() {
        return assetId;
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
     * Returns the owner's Identity identifier.
     *
     * @return the owner's Identity identifier
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Returns the quantity of the Account (Asset) or {@code null} if not applicable.
     *
     * @return the quantity of the Account (Asset) or {@code null} if not applicable
     */
    public BigInteger getQuantity() {
        return quantity;
    }
}
