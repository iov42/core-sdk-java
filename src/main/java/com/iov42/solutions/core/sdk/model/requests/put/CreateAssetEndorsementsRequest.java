package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.Map;

/**
 * Data structure used to create endorsements against asset claims.
 */
public class CreateAssetEndorsementsRequest extends CreateEndorsementsRequest {

    public CreateAssetEndorsementsRequest(String subjectId, String endorserId, Map<String, String> endorsements) {
        super(TransactionType.CreateAssetEndorsementsRequest, subjectId, endorserId, endorsements);
    }

    public CreateAssetEndorsementsRequest(String requestId, String subjectId, String endorserId, Map<String, String> endorsements) {
        super(requestId, TransactionType.CreateAssetEndorsementsRequest, subjectId, endorserId, endorsements);
    }
}
