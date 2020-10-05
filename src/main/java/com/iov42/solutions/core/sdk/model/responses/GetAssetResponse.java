package com.iov42.solutions.core.sdk.model.responses;

import java.math.BigInteger;

public class GetAssetResponse extends BaseResponse {

    private String assetId;

    private String assetTypeId;

    private String ownerId;

    private BigInteger quantity;

    private GetAssetResponse() {
        // needed for Jackson parser
    }

    public GetAssetResponse(String proof, String assetId, String assetTypeId, String ownerId, BigInteger quantity) {
        super(proof);
        this.assetId = assetId;
        this.assetTypeId = assetTypeId;
        this.ownerId = ownerId;
        this.quantity = quantity;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public BigInteger getQuantity() {
        return quantity;
    }
}
