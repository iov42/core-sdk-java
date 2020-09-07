package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.Claims;

/**
 * Request structure for creating claims for an AssetType.
 */
@JsonTypeName("CreateAssetTypeClaimsRequest")
public class CreateAssetTypeClaimsRequest extends CreateClaimsRequest {

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier (subject)
     * @param claims      the {@link Claims} to be created
     */
    public CreateAssetTypeClaimsRequest(String assetTypeId, Claims claims) {
        this(null, assetTypeId, claims);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier (subject)
     * @param claims      the {@link Claims} to be created
     */
    public CreateAssetTypeClaimsRequest(String requestId, String assetTypeId, Claims claims) {
        super(requestId, null, assetTypeId, claims);
    }
}
