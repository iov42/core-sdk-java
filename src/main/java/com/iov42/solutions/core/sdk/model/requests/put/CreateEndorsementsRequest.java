package com.iov42.solutions.core.sdk.model.requests.put;

import com.iov42.solutions.core.sdk.model.KeyInfo;

import java.util.List;
import java.util.Map;

/**
 * Data structure used to create endorsements for specified claims PUT request.
 */
public abstract class CreateEndorsementsRequest extends BasePutRequest {

    /**
     * A non-empty map where the keys are the hashed claims and the values are the signatures by the endorser of these hashes.
     * To create this map for identity claims call this method:
     * {@link com.iov42.solutions.core.sdk.PlatformClient#makeEndorsements(KeyInfo keyPair, String subjectId, List plainClaims)}
     * or to create this map for asset claims or asset type call this method:
     * {@link com.iov42.solutions.core.sdk.PlatformClient#makeEndorsements(KeyInfo keyPair, String subjectId, String subjectTypeId, List plainClaims)}
     */
    private final Map<String, String> endorsements;

    /**
     * The identity identifier who is endorsing the claim.
     */
    private final String endorserId;

    /**
     * The subject identifier to which the claims are linked.
     */
    private final String subjectId;

    protected CreateEndorsementsRequest(TransactionType _type, String subjectId, String endorserId, Map<String, String> endorsements) {
        super(_type);
        this.subjectId = subjectId;
        this.endorserId = endorserId;
        this.endorsements = endorsements;
    }

    protected CreateEndorsementsRequest(String requestId, TransactionType _type, String subjectId, String endorserId, Map<String, String> endorsements) {
        super(requestId, _type);
        this.subjectId = subjectId;
        this.endorserId = endorserId;
        this.endorsements = endorsements;
    }

    public Map<String, String> getEndorsements() {
        return endorsements;
    }

    public String getEndorserId() {
        return endorserId;
    }

    public String getSubjectId() {
        return subjectId;
    }
}
