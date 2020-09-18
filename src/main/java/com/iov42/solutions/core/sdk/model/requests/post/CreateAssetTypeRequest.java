package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.AssetTypeProperty;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class CreateAssetTypeRequest extends BaseRequest {

    private String assetTypeId;

    private String type;

    public CreateAssetTypeRequest() {
        super();
    }

    public CreateAssetTypeRequest(String assetTypeId, AssetTypeProperty type) {
        super();
        this.assetTypeId = assetTypeId;
        this.type = type.getType();
    }

    public CreateAssetTypeRequest(String requestId, String assetTypeId, AssetTypeProperty type) {
        super(requestId);
        this.assetTypeId = assetTypeId;
        this.type = type.getType();
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CreateAssetTypeRequest{" +
                "assetTypeId='" + assetTypeId + '\'' +
                ", type=" + type +
                "} " + super.toString();
    }
}
