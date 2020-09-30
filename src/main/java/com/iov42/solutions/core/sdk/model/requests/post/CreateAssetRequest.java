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

    //private String quantity;


    public String getAssetId() {
        return assetId;
    }


    public String getAssetTypeId() {
        return assetTypeId;
    }


//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }

    @Override
    public String toString() {
        return "CreateAssetRequest{" +
                "assetId='" + assetId + '\'' +
                ", assetTypeId='" + assetTypeId + '\'' +
                //    ", quantity='" + quantity + '\'' +
                "} " + super.toString();
    }
}
