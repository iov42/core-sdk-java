package com.iov42.solutions.core.sdk.model.requests;

import com.iov42.solutions.core.sdk.model.AssetTypeProperty;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class CreateAssetTypeRequest extends BaseRequest {

    private String assetTypeId;

    private int scale;

    private AssetTypeProperty type;

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public AssetTypeProperty getType() {
        return type;
    }

    public void setType(AssetTypeProperty type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CreateAssetTypeRequest{" +
                "assetTypeId='" + assetTypeId + '\'' +
                ", scale=" + scale +
                ", type=" + type +
                "} " + super.toString();
    }
}
