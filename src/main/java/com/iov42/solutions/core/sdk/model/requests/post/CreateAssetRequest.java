package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class CreateAssetRequest extends BaseRequest {

    private final String assetId;

    private final String assetTypeId;

    public CreateAssetRequest(String requestId, String assetId, String assetTypeId) {
        super(requestId);
        this.assetId = assetId;
        this.assetTypeId = assetTypeId;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    @Override
    public String toString() {
        return "CreateAssetRequest{" +
                "assetId='" + assetId + '\'' +
                ", assetTypeId='" + assetTypeId + '\'' +
                "} " + super.toString();
    }
}
