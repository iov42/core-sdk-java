package com.iov42.solutions.core.sdk.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Structure represents information of a signature.
 */
public class SignatureInfo {

    private final String identityId;

    private final ProtocolType protocolId;

    private final String signature;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String delegateIdentityId;

    /**
     * Initializes a new signature object.
     *
     * @param identityId the Identity identifier
     * @param protocolId the {@link ProtocolType}
     * @param signature  the signature (as Base64 encoded {@code String} representation)
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SignatureInfo(@JsonProperty("identityId") String identityId,
                         @JsonProperty("protocolId") ProtocolType protocolId,
                         @JsonProperty("signature") String signature) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.signature = signature;
    }

    /**
     * Initializes a new signature object.
     *
     * @param identityId         the Identity identifier
     * @param delegateIdentityId the delegate Identity identifier
     * @param protocolId         the {@link ProtocolType}
     * @param signature          the signature (as Base64 encoded {@code String} representation)
     */
    public SignatureInfo(String identityId, String delegateIdentityId, ProtocolType protocolId, String signature) {
        this(identityId, protocolId, signature);
        this.delegateIdentityId = delegateIdentityId;
    }

    /**
     * Returns the delegate Identity identifier.
     *
     * @return the delegate Identity identifier or {@code null}
     */
    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    /**
     * Returns the Identity identifier.
     *
     * @return the Identity identifier
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * Returns the {@link ProtocolType}.
     *
     * @return the {@link ProtocolType}
     */
    public ProtocolType getProtocolId() {
        return protocolId;
    }

    /**
     * Returns the signature (as Base64 encoded {@code String} representation).
     *
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }
}
