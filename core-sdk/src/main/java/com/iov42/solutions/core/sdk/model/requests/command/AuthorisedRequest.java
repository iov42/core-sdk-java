package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iov42.solutions.core.sdk.model.SignatoryInfo;
import com.iov42.solutions.core.sdk.model.SignatureInfo;
import com.iov42.solutions.core.sdk.utils.Constants;
import com.iov42.solutions.core.sdk.utils.serialization.JsonUtils;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.util.*;

/**
 * Structure for authorised requests.
 */
public class AuthorisedRequest {

    final List<SignatureInfo> authorisations = new ArrayList<>();

    final String requestId;

    final String body;

    final List<String> headers = new ArrayList<>();

    final boolean requireClaimHeaderCheck;

    private AuthorisedRequest(String requestId, String body, boolean requireClaimHeaderCheck) {
        this.requestId = requestId;
        this.body = body;
        this.requireClaimHeaderCheck = requireClaimHeaderCheck;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AuthorisedRequest(@JsonProperty("requestId") String requestId,
                             @JsonProperty("body") String body,
                             @JsonProperty("headers") List<String> headers,
                             @JsonProperty("requireHeaderCheck") boolean requireClaimHeaderCheck,
                             @JsonProperty("authorisations") List<SignatureInfo> authorisations) {

        this(requestId, body, requireClaimHeaderCheck);
        this.authorisations.addAll(authorisations);
        this.headers.addAll(headers);
    }

    /**
     * Creates an instance of an {@link AuthorisedRequest} from a source {@link BaseCommandRequest}.
     *
     * @param commandRequest the source {@link BaseCommandRequest}
     * @return the {@link AuthorisedRequest} instance
     */
    public static AuthorisedRequest from(BaseCommandRequest commandRequest) {
        String body = JsonUtils.toJson(commandRequest);
        if (commandRequest instanceof ClaimsContainerRequest) {
            ClaimsContainerRequest claimsContainer = (ClaimsContainerRequest) commandRequest;
            AuthorisedRequest request = new AuthorisedRequest(commandRequest.getRequestId(), body, claimsContainer.isEndorsement());
            request.addHeader(Constants.HEADER_IOV42_CLAIMS,
                    PlatformUtils.getEncodedHeaderValue(PlatformUtils.claimMap(claimsContainer.getClaims())));
            return request;
        }
        return new AuthorisedRequest(commandRequest.getRequestId(), body, false);
    }

    /**
     * Creates an instance of an {@link AuthorisedRequest} from a serialized {@code String}.
     *
     * @param serialized the serialized {@code String}
     * @return the {@link AuthorisedRequest} instance
     */
    public static AuthorisedRequest from(String serialized) {
        return JsonUtils.fromJson(serialized, AuthorisedRequest.class);
    }

    /**
     * Serializes the request to a {@code String}.
     *
     * @return the serialized {@code String.}
     */
    public String serialize() {
        return JsonUtils.toJson(this);
    }

    /**
     * Returns the request identifier.
     *
     * @return the request identifier
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns the request body.
     *
     * @return the request body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns a list of authorisations ({@link SignatureInfo}).
     *
     * @return a list of authorisations
     */
    public List<SignatureInfo> getAuthorisations() {
        return authorisations;
    }

    /**
     * Returns whether the request requires a check for the claims header.
     *
     * @return {@code true} if check is required, {@code false} otherwise
     */
    public boolean isRequireClaimHeaderCheck() {
        return requireClaimHeaderCheck;
    }

    /**
     * Adds an authorisation to the request.
     *
     * @param signatoryInfo the authorizer
     * @return the {@link AuthorisedRequest}
     */
    public AuthorisedRequest authorise(SignatoryInfo signatoryInfo) {
        authorisations.add(PlatformUtils.sign(signatoryInfo, body));
        return this;
    }

    private void addHeader(String key, String value) {
        headers.add(key);
        headers.add(value);
    }

    /**
     * Returns a collection of additional request headers.
     *
     * @return a collection of additional request headers
     */
    public Collection<String> getHeaders() {
        return headers;
    }
}
