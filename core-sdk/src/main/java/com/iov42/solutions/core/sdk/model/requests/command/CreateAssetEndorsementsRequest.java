package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.Endorsements;

/**
 * Request structure for endorsing claims of an Asset.
 */
@JsonTypeName("CreateAssetEndorsementsRequest")
public class CreateAssetEndorsementsRequest extends CreateEndorsementsRequest {

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId  the AssetType identifier (subject type)
     * @param assetId      the Asset identifier (subject)
     * @param endorserId   the endorser identifier
     * @param endorsements the {@link Endorsements}
     */
    public CreateAssetEndorsementsRequest(String assetTypeId, String assetId, String endorserId, Endorsements endorsements) {
        this(null, assetTypeId, assetId, endorserId, endorsements);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId    a specific request identifier
     * @param assetTypeId  the AssetType identifier (subject type)
     * @param assetId      the Asset identifier (subject)
     * @param endorserId   the endorser identifier
     * @param endorsements the {@link Endorsements}
     */
    public CreateAssetEndorsementsRequest(String requestId, String assetTypeId, String assetId, String endorserId, Endorsements endorsements) {
        super(requestId, assetTypeId, assetId, endorserId, endorsements);
    }
}
