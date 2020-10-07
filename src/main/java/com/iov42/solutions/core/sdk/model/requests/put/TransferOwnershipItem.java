package com.iov42.solutions.core.sdk.model.requests.put;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TransferOwnershipItem {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String assetId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String assetTypeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fromIdentityId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toIdentityId;

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
