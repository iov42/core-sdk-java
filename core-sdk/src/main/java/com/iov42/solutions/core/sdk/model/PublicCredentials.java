package com.iov42.solutions.core.sdk.model;

/**
 * Structure that represents the public credentials of an Identity.
 */
public class PublicCredentials {

    private String key;

    private ProtocolType protocolId;

    private PublicCredentials() {
    }

    /**
     * Initializes a new credentials object
     *
     * @param protocolId the protocol type
     * @param key        (as Base64 encoded {@code String}) of an Identity
     */
    public PublicCredentials(ProtocolType protocolId, String key) {
        this.key = key;
        this.protocolId = protocolId;
    }

    /**
     * Returns the public key (as Base64 encoded {@code String}) of the Identity.
     *
     * @return the public key of the Identity
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the {@link ProtocolType}.
     *
     * @return the {@link ProtocolType}
     */
    public ProtocolType getProtocolId() {
        return protocolId;
    }
}
