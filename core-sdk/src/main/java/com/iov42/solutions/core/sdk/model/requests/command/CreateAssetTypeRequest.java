package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.AssetTypeType;

/**
 * Request structure for creating an AssetType.
 */
@JsonTypeName("DefineAssetTypeRequest")
public class CreateAssetTypeRequest extends BaseCommandRequest {

    private final String assetTypeId;

    private final String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer scale;

    private CreateAssetTypeRequest(String requestId, String assetTypeId, AssetTypeType assetTypeType, Integer scale) {
        super(requestId);
        this.assetTypeId = assetTypeId;
        this.type = assetTypeType.getType();
        this.scale = scale;
    }

    /**
     * Initializes a new request object. This variant is for unique AssetTypes.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     */
    public CreateAssetTypeRequest(String assetTypeId) {
        this(null, assetTypeId);
    }

    /**
     * Initializes a new request object. This variant is for unique AssetTypes.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     */
    public CreateAssetTypeRequest(String requestId, String assetTypeId) {
        this(requestId, assetTypeId, AssetTypeType.UNIQUE, null);
    }

    /**
     * Initializes a new request object. This variant is for quantifiable AssetTypes.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param scale       scale of the quantifiable AssetType
     */
    public CreateAssetTypeRequest(String requestId, String assetTypeId, int scale) {
        this(requestId, assetTypeId, AssetTypeType.QUANTIFIABLE, scale);
    }

    /**
     * Initializes a new request object. This variant is for quantifiable AssetTypes.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param scale       scale of the quantifiable AssetType
     */
    public CreateAssetTypeRequest(String assetTypeId, int scale) {
        this(null, assetTypeId, AssetTypeType.QUANTIFIABLE, scale);
    }

    /**
     * Creates a new quantifiable AssetType request object.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param scale       scale of the quantifiable AssetType
     */
    public static CreateAssetTypeRequest quantifiable(String assetTypeId, int scale) {
        return quantifiable(null, assetTypeId, scale);
    }

    /**
     * Creates a new quantifiable AssetType request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param scale       scale of the quantifiable AssetType
     */
    public static CreateAssetTypeRequest quantifiable(String requestId, String assetTypeId, int scale) {
        return new CreateAssetTypeRequest(requestId, assetTypeId, scale);
    }

    /**
     * Creates a new unique AssetType request object.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     */
    public static CreateAssetTypeRequest unique(String assetTypeId) {
        return unique(null, assetTypeId);
    }

    /**
     * Creates a new unique AssetType request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     */
    public static CreateAssetTypeRequest unique(String requestId, String assetTypeId) {
        return new CreateAssetTypeRequest(requestId, assetTypeId);
    }

    /**
     * Returns the AssetType identifier.
     *
     * @return the AssetType identifier
     */
    public String getAssetTypeId() {
        return assetTypeId;
    }

    /**
     * Returns the scale of the AssetType (for quantifiable assets only).
     *
     * @return the scale of the quantifiable AssetType or ({@code null} for a unique Asset
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * Returns the type of the AssetType (quantifiable or unique).
     *
     * @return the type of the AssetType
     */
    public String getType() {
        return type;
    }
}
