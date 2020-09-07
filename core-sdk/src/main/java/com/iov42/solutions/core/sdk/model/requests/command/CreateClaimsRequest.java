package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iov42.solutions.core.sdk.model.Claims;

/**
 * Base Request structure for creating claims.
 */
public abstract class CreateClaimsRequest extends BaseCommandRequest implements ClaimsContainerRequest {

    private final String subjectId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String subjectTypeId;

    private final Claims claims;

    protected CreateClaimsRequest(String requestId, String subjectTypeId, String subjectId, Claims claims) {
        super(requestId);
        this.subjectTypeId = subjectTypeId;
        this.subjectId = subjectId;
        this.claims = claims;
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
     * Returns the identifier of the subject type (depends on the type of request; could be {@code null} or
     * represent an AssetType).
     *
     * @return the identifier of the subject type
     */
    public String getSubjectTypeId() {
        return subjectTypeId;
    }

    /**
     *  Returns the {@link Claims} to be created.
     *
     * @return the {@link Claims} to be created
     */
    @Override
    public Claims getClaims() {
        return claims;
    }

    /**
     * Returns {@code false} as this is not an endorsement request.
     *
     * @return {@code false} as this is not an endorsement request
     */
    @Override
    public boolean isEndorsement() {
        return false;
    }
}
