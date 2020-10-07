package com.iov42.solutions.core.sdk.model.requests.put;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

/**
 * Data structure used to create asset account or unique asset.
 * In case it's asset account, quantity is mandatory.
 */
public class CreateAssetRequest extends BasePutRequest {

    /**
     * The identifier of the new asset. This should be unique within the asset type namespace.
     */
    private final String assetId;

    /**
     * The identifier of the asset type in which the new asset will belong.
     */
    private final String assetTypeId;

    /**
     * The initial amount of this account to be created at the given request. Must be a whole, positive number.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger quantity;

    public CreateAssetRequest(String assetTypeId, String assetId) {
        super(TransactionType.CreateAssetRequest);
        this.assetTypeId = assetTypeId;
        this.assetId = assetId;
    }

    public CreateAssetRequest(String assetTypeId, String assetId, BigInteger quantity) {
        super(TransactionType.CreateAssetRequest);
        this.assetTypeId = assetTypeId;
        this.assetId = assetId;
        this.quantity = quantity;
    }

    public CreateAssetRequest(String requestId, String assetTypeId, String assetId) {
        super(requestId, TransactionType.CreateAssetRequest);
        this.assetTypeId = assetTypeId;
        this.assetId = assetId;
    }

    public CreateAssetRequest(String requestId, String assetTypeId, String assetId, BigInteger quantity) {
        super(requestId, TransactionType.CreateAssetRequest);
        this.assetTypeId = assetTypeId;
        this.assetId = assetId;
        this.quantity = quantity;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public BigInteger getQuantity() {
        return quantity;
    }
}
