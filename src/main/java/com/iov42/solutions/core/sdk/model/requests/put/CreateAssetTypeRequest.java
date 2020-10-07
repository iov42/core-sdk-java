package com.iov42.solutions.core.sdk.model.requests.put;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iov42.solutions.core.sdk.model.AssetTypeProperty;

/**
 * Data structure used to create asset type.
 */
public class CreateAssetTypeRequest extends BasePutRequest {

    /**
     * The identifier of this asset type.
     */
    private final String assetTypeId;

    /**
     * Possible values: "Unique" or "Quantifiable";
     * Whether instances of the asset type are entities or quantities.
     */
    private final String type;

    /**
     * Specifies the maximum number of decimal places that may be represented for a quantity of the given type.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer scale;

    public CreateAssetTypeRequest(String assetTypeId, AssetTypeProperty type) {
        super(TransactionType.DefineAssetTypeRequest);
        this.assetTypeId = assetTypeId;
        this.type = type.getType();
    }

    public CreateAssetTypeRequest(String requestId, String assetTypeId, AssetTypeProperty type) {
        super(requestId, TransactionType.DefineAssetTypeRequest);
        this.assetTypeId = assetTypeId;
        this.type = type.getType();
    }

    public CreateAssetTypeRequest(String requestId, String assetTypeId, AssetTypeProperty type, Integer scale) {
        super(requestId, TransactionType.DefineAssetTypeRequest);
        this.assetTypeId = assetTypeId;
        this.type = type.getType();
        this.scale = scale;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public Integer getScale() {
        return scale;
    }

    public String getType() {
        return type;
    }
}
