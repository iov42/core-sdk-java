package com.iov42.solutions.core.sdk.model.requests.get;

public class GetAssetTypeRequest extends BaseGetRequest {

    private final String assetTypeId;

    public GetAssetTypeRequest(String requestId, String assetTypeId) {
        super(requestId);
        this.assetTypeId = assetTypeId;
    }

    public GetAssetTypeRequest(String requestId, String nodeId, String assetTypeId) {
        super(requestId, nodeId);
        this.assetTypeId = assetTypeId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    @Override
    public String toString() {
        return "GetAssetTypeRequest{" +
                "assetTypeId='" + assetTypeId + '\'' +
                "} " + super.toString();
    }
}
