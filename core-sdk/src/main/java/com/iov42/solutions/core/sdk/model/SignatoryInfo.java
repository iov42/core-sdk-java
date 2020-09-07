package com.iov42.solutions.core.sdk.model;

import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.security.KeyPair;

/**
 * Structure that represents a signatory for creating platform conform signatures.
 */
public class SignatoryInfo {

    private final String identityId;

    private final byte[] privateKey;

    private final ProtocolType protocolId;

    private final byte[] publicKey;

    private final String delegateIdentityId;

    /**
     * Initializes a new signatory object.
     *
     * @param identityId         the Identity identifier
     * @param protocolId         the {@link ProtocolType}
     * @param publicKey          the public key bytes
     * @param privateKey         the private key bytes
     * @param delegateIdentityId the delegate Identity identifier
     */
    public SignatoryInfo(String identityId, ProtocolType protocolId, byte[] publicKey, byte[] privateKey, String delegateIdentityId) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.delegateIdentityId = delegateIdentityId;
    }

    /**
     * Initializes a new signatory object.
     *
     * @param identityId the Identity identifier
     * @param protocolId the {@link ProtocolType}
     * @param publicKey  the public key bytes
     * @param privateKey the private key bytes
     */
    public SignatoryInfo(String identityId, ProtocolType protocolId, byte[] publicKey, byte[] privateKey) {
        this(identityId, protocolId, publicKey, privateKey, null);
    }

    /**
     * Initializes a new signatory object.
     *
     * @param identityId         the Identity identifier
     * @param protocolId         the {@link ProtocolType}
     * @param keyPair            the {@link KeyPair}
     * @param delegateIdentityId the delegate Identity identifier
     */
    public SignatoryInfo(String identityId, ProtocolType protocolId, KeyPair keyPair, String delegateIdentityId) {
        this(identityId, protocolId, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded(), delegateIdentityId);
    }

    /**
     * Initializes a new signatory object.
     *
     * @param identityId the Identity identifier
     * @param protocolId the {@link ProtocolType}
     * @param keyPair    the {@link KeyPair}
     */
    public SignatoryInfo(String identityId, ProtocolType protocolId, KeyPair keyPair) {
        this(identityId, protocolId, keyPair, null);
    }

    /**
     * Returns the delegate Identity identifier (optional).
     *
     * @return the delegate Identity identifier
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
     * Returns the private key bytes.
     *
     * @return the private key bytes
     */
    public byte[] getPrivateKey() {
        return privateKey;
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
     * Returns the public key bytes.
     *
     * @return the public key bytes
     */
    public byte[] getPublicKey() {
        return publicKey;
    }

    /**
     * Converts the {@link SignatureInfo} to a {@link PublicCredentials} representation.
     *
     * @return a {@link PublicCredentials} representation
     */
    public PublicCredentials toPublicCredentials() {
        if (publicKey == null) {
            throw new RuntimeException("No public key specified!");
        }
        return new PublicCredentials(protocolId, PlatformUtils.encodeBase64(publicKey));
    }
}
