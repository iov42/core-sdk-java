package com.iov42.solutions.core.sdk.model;

public class CreateAssetRequest extends BaseRequest {

    private String assetId;

    private String assetTypeId;

    private String quantity;

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CreateAssetRequest{" +
                "assetId='" + assetId + '\'' +
                ", assetTypeId='" + assetTypeId + '\'' +
                ", quantity='" + quantity + '\'' +
                "} " + super.toString();
    }
}
