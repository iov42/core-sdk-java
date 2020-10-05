package com.iov42.solutions.core.sdk.model.requests.post;

public class TransferOwnershipItem {

    private final String assetId;

    private final String assetTypeId;

    private final String fromIdentityId;

    private final String toIdentityId;

    public TransferOwnershipItem(String assetId, String assetTypeId, String fromIdentityId, String toIdentityId) {
        this.assetId = assetId;
        this.assetTypeId = assetTypeId;
        this.fromIdentityId = fromIdentityId;
        this.toIdentityId = toIdentityId;
    }

    public String getAssetId() {
        return assetId;
    }


    public String getAssetTypeId() {
        return assetTypeId;
    }


    public String getFromIdentityId() {
        return fromIdentityId;
    }


    public String getToIdentityId() {
        return toIdentityId;
    }

}
