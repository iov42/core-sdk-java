package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.List;

/**
 * Data structure used to create identity claims.
 */
public class CreateIdentityClaimsRequest extends CreateClaimsRequest {

    public CreateIdentityClaimsRequest(String subjectId, List<String> claims) {
        super(TransactionType.CreateIdentityClaimsRequest, subjectId, claims);
    }

    public CreateIdentityClaimsRequest(String requestId, String subjectId, List<String> claims) {
        super(requestId, TransactionType.CreateIdentityClaimsRequest, subjectId, claims);
    }
}
