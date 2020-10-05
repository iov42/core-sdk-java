package com.iov42.solutions.core.sdk.model.requests.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

public class CreateAssetAccountRequest extends BaseCreateAssetRequest {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger quantity;

    public CreateAssetAccountRequest(String requestId, String assetId, String assetTypeId, BigInteger quantity) {
        super(requestId, assetId, assetTypeId);
        this.quantity = quantity;
    }

    public BigInteger getQuantity() {
        return quantity;
    }
}
