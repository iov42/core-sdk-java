package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.Map;

/**
 * Data structure used to create endorsements against asset type claims.
 */
public class CreateAssetTypeEndorsementsRequest extends CreateEndorsementsRequest {

    public CreateAssetTypeEndorsementsRequest(String subjectId, String endorserId, Map<String, String> endorsements) {
        super(TransactionType.CreateAssetTypeEndorsementsRequest, subjectId, endorserId, endorsements);
    }

    public CreateAssetTypeEndorsementsRequest(String requestId, String subjectId, String endorserId, Map<String, String> endorsements) {
        super(requestId, TransactionType.CreateAssetTypeEndorsementsRequest, subjectId, endorserId, endorsements);
    }
}
