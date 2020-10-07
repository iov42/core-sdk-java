package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.Map;

/**
 * Data structure used to create endorsements against identity claims.
 */
public class CreateIdentityEndorsementsRequest extends CreateEndorsementsRequest {

    public CreateIdentityEndorsementsRequest(String subjectId, String endorserId, Map<String, String> endorsements) {
        super(TransactionType.CreateIdentityEndorsementsRequest, subjectId, endorserId, endorsements);
    }

    public CreateIdentityEndorsementsRequest(String requestId, String subjectId, String endorserId, Map<String, String> endorsements) {
        super(requestId, TransactionType.CreateIdentityEndorsementsRequest, subjectId, endorserId, endorsements);
    }
}
