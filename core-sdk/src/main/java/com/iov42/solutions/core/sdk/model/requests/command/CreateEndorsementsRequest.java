package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iov42.solutions.core.sdk.model.Claims;
import com.iov42.solutions.core.sdk.model.Endorsements;

/**
 * Base request structure for endorsing claims of an entity.
 */
public abstract class CreateEndorsementsRequest extends BaseCommandRequest implements ClaimsContainerRequest {

    private final Endorsements endorsements;

    private final String endorserId;

    private final String subjectId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String subjectTypeId;

    protected CreateEndorsementsRequest(String requestId, String subjectTypeId, String subjectId, String endorserId, Endorsements endorsements) {
        super(requestId);
        this.subjectTypeId = subjectTypeId;
        this.subjectId = subjectId;
        this.endorserId = endorserId;
        this.endorsements = endorsements;
    }

    /**
     * Returns the {@link Endorsements}.
     *
     * @return the {@link Endorsements}
     */
    public Endorsements getEndorsements() {
        return endorsements;
    }

    /**
     * Returns the identifier of the endorser (Identity).
     *
     * @return the identifier of the endorser
     */
    public String getEndorserId() {
        return endorserId;
    }

    /**
     * Returns the identifier of the subject type (depends on the type of request; could be {@code null} or
     * represent an AssetType).
     *
     * @return the identifier of the subject type
     */
    public String getSubjectTypeId() {
        return subjectTypeId;
    }

    /**
     * Returns the identifier of the subject (depends on the type of request; could represent an Identity, Asset or AssetType).
     *
     * @return the identifier of the subject
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     *  Returns the {@link Claims} to be endorsed.
     *
     * @return the {@link Claims} to be endorsed
     */
    @Override
    public Claims getClaims() {
        return endorsements.getClaims();
    }

    /**
     * Returns {@code true} as this is an endorsement request.
     *
     * @return {@code true} as this is an endorsement request
     */
    @Override
    public boolean isEndorsement() {
        return true;
    }
}
