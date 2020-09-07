package com.iov42.solutions.core.sdk.model.responses;

/**
 * Structure represents information of an AssetType.
 */
public class GetAssetTypeResponse extends BaseResponse {

    private String assetTypeId;

    private String ownerId;

    private Integer scale;

    private String type;

    private GetAssetTypeResponse() {
    }

    GetAssetTypeResponse(String proof, String assetTypeId, String ownerId, String type, Integer scale) {
        super(proof);
        this.assetTypeId = assetTypeId;
        this.ownerId = ownerId;
        this.type = type;
        this.scale = scale;
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
     * Returns the owner's identity identifier.
     *
     * @return the owner's identity identifier
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Returns the scale of the AssetType or {@code null} if not applicable.
     *
     * @return the scale of the AssetType or {@code null} if not applicable
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * Returns the type of the AssetType.
     *
     * @return the type of the AssetType
     */
    public String getType() {
        return type;
    }
}
