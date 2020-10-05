package com.iov42.solutions.core.sdk.model.responses;

public class GetAssetTypeResponse extends BaseResponse {

    private String assetTypeId;

    private String ownerId;

    private Integer scale;

    private String type;

    private GetAssetTypeResponse() {
        // needed for Jackson parser
    }

    public GetAssetTypeResponse(String proof, String assetTypeId, String ownerId, String type, Integer scale) {
        super(proof);
        this.assetTypeId = assetTypeId;
        this.ownerId = ownerId;
        this.type = type;
        this.scale = scale;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getScale() {
        return scale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
