package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.List;

/**
 * Data structure used to create asset type claims.
 */
public class CreateAssetTypeClaimsRequest extends CreateClaimsRequest {

    public CreateAssetTypeClaimsRequest(String subjectId, String subjectTypeId, List<String> claims) {
        super(TransactionType.CreateAssetTypeClaimsRequest, subjectId, subjectTypeId, claims);
    }

    public CreateAssetTypeClaimsRequest(String requestId, String subjectId, String subjectTypeId, List<String> claims) {
        super(requestId, TransactionType.CreateAssetTypeClaimsRequest, subjectId, subjectTypeId, claims);
    }
}
