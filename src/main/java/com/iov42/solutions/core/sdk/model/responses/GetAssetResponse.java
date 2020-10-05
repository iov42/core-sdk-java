package com.iov42.solutions.core.sdk.model.responses;

public class GetAssetResponse extends BaseResponse {

    private final String assetId;
    private final String assetTypeId;
    private final String ownerId;


    public GetAssetResponse(String proof, String assetId, String assetTypeId, String ownerId) {
        super(proof);
        this.assetId = assetId;
        this.assetTypeId = assetTypeId;
        this.ownerId = ownerId;
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
}
