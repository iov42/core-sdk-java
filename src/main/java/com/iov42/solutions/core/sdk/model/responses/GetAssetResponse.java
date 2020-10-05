package com.iov42.solutions.core.sdk.model.responses;

public class GetAssetResponse extends BaseResponse {

    private String assetId;

    private String assetTypeId;

    private String ownerId;

    private String quantity;

    private GetAssetResponse() {
        // needed for Jackson parser
    }

    public GetAssetResponse(String proof, String assetId, String assetTypeId, String ownerId, String quantity) {
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

    public String getQuantity() {
        return quantity;
    }
}
