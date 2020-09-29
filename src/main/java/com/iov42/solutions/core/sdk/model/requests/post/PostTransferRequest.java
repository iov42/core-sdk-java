package com.iov42.solutions.core.sdk.model.requests.post;

public class PostTransferRequest {

    private String assetId;

    private String assetTypeId;

    private String fromIdentityId;

    private String toIdentityId;

    public PostTransferRequest(String assetId, String assetTypeId, String fromIdentityId, String toIdentityId) {
        this.assetId = assetId;
        this.assetTypeId = assetTypeId;
        this.fromIdentityId = fromIdentityId;
        this.toIdentityId = toIdentityId;
    }

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

    public String getFromIdentityId() {
        return fromIdentityId;
    }

    public void setFromIdentityId(String fromIdentityId) {
        this.fromIdentityId = fromIdentityId;
    }

    public String getToIdentityId() {
        return toIdentityId;
    }

    public void setToIdentityId(String toIdentityId) {
        this.toIdentityId = toIdentityId;
    }
}
