package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving information of a claim of an AssetType.
 */
public class GetAssetTypeClaimRequest extends GetAssetTypeRequest {

    private final String hashedClaim;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param hashedClaim the hashed claim to be queried
     */
    public GetAssetTypeClaimRequest(String assetTypeId, String hashedClaim) {
        this(null, assetTypeId, hashedClaim);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param hashedClaim the hashed claim to be queried
     */
    public GetAssetTypeClaimRequest(String requestId, String assetTypeId, String hashedClaim) {
        super(requestId, assetTypeId);
        this.hashedClaim = hashedClaim;
    }

    /**
     * Returns the hashed claim.
     *
     * @return the hashed claim
     */
    public String getHashedClaim() {
        return hashedClaim;
    }

}
