package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iov42.solutions.core.sdk.model.Endorsements;

/**
 * Request structure for endorsing claims of an AssetType.
 */
@JsonTypeName("CreateAssetTypeEndorsementsRequest")
public class CreateAssetTypeEndorsementsRequest extends CreateEndorsementsRequest {

    /**
     * Initializes a new request object. A random request identifier will be used.
     *
     * @param assetTypeId  the AssetType identifier
     * @param endorserId   the endorser identifier
     * @param endorsements the {@link Endorsements}
     */
    public CreateAssetTypeEndorsementsRequest(String assetTypeId, String endorserId, Endorsements endorsements) {
        this(null, assetTypeId, endorserId, endorsements);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId    a specific request identifier
     * @param assetTypeId  the AssetType identifier
     * @param endorserId   the endorser identifier
     * @param endorsements the {@link Endorsements}
     */
    public CreateAssetTypeEndorsementsRequest(String requestId, String assetTypeId, String endorserId, Endorsements endorsements) {
        super(requestId, null, assetTypeId, endorserId, endorsements);
    }
}
