package com.iov42.solutions.core.sdk.model.requests.get;

public class GetAssetRequest extends GetAssetTypeRequest {

    private final String assetId;

    public GetAssetRequest(String requestId, String nodeId, String assetTypeId, String assetId) {
        super(requestId, nodeId, assetTypeId);
        this.assetId = assetId;
    }

    public String getAssetId() {
        return assetId;
    }
}
