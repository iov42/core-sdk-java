package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * Request structure for retrieving Asset claim information.
 */
public class GetAssetClaimRequest extends GetAssetRequest {

    private final String hashedClaim;

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId an AssetType identifier
     * @param assetId     a Asset identifier
     * @param hashedClaim the hashed claim to be queried
     */
    public GetAssetClaimRequest(String requestId, String assetTypeId, String assetId, String hashedClaim) {
        super(requestId, assetTypeId, assetId);
        this.hashedClaim = hashedClaim;
    }

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId an AssetType identifier
     * @param assetId     a Asset identifier
     * @param hashedClaim the hashed claim to be queried
     */
    public GetAssetClaimRequest(String assetTypeId, String assetId, String hashedClaim) {
        this(null, assetTypeId, assetId, hashedClaim);
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
