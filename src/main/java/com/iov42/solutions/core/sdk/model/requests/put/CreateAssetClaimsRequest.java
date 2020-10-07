package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.List;

/**
 * Data structure used to create asset claims.
 */
public class CreateAssetClaimsRequest extends CreateClaimsRequest {

    public CreateAssetClaimsRequest(String subjectId, String subjectTypeId, List<String> claims) {
        super(TransactionType.CreateAssetClaimsRequest, subjectId, subjectTypeId, claims);
    }

    public CreateAssetClaimsRequest(String requestId, String subjectId, String subjectTypeId, List<String> claims) {
        super(requestId, TransactionType.CreateAssetClaimsRequest, subjectId, subjectTypeId, claims);
    }
}
