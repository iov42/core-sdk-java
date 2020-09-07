package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

/**
 * Base class for command requests.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddDelegateRequest.class),
        @JsonSubTypes.Type(value = CreateAssetClaimsRequest.class),
        @JsonSubTypes.Type(value = CreateAssetEndorsementsRequest.class),
        @JsonSubTypes.Type(value = CreateAssetRequest.class),
        @JsonSubTypes.Type(value = CreateAssetTypeClaimsRequest.class),
        @JsonSubTypes.Type(value = CreateAssetTypeEndorsementsRequest.class),
        @JsonSubTypes.Type(value = CreateAssetTypeRequest.class),
        @JsonSubTypes.Type(value = CreateIdentityClaimsRequest.class),
        @JsonSubTypes.Type(value = CreateIdentityEndorsementsRequest.class),
        @JsonSubTypes.Type(value = CreateIdentityRequest.class),
        @JsonSubTypes.Type(value = TransfersRequest.class),
})
public abstract class BaseCommandRequest extends BaseRequest {

    protected BaseCommandRequest(String requestId) {
        super(requestId);
    }
}
