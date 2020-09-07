package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.Claims;

/**
 * Request structure for creating claims for an Asset.
 */
@JsonTypeName("CreateAssetClaimsRequest")
public class CreateAssetClaimsRequest extends CreateClaimsRequest {

    /**
     * Initializes a new request object.
     *
     * @param assetId     the Asset identifier (subject)
     * @param assetTypeId the AssetType identifier (subject type)
     * @param claims      the claims to be created
     */
    public CreateAssetClaimsRequest(String assetTypeId, String assetId, Claims claims) {
        this(null, assetTypeId, assetId, claims);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   the request identifier
     * @param assetId     the Asset identifier (subject)
     * @param assetTypeId the AssetType identifier (subject type)
     * @param claims      the claims to be created
     */
    public CreateAssetClaimsRequest(String requestId, String assetTypeId, String assetId, Claims claims) {
        super(requestId, assetTypeId, assetId, claims);
    }
}
