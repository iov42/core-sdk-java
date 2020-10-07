package com.iov42.solutions.core.sdk.model;

import java.security.KeyPair;

/**
 * A {@link KeyPair} wrapper that also holds protocol type, identityId and optionally delegateIdentityId.
 */
public class IovKeyPair {

    private final String identityId;

    private final byte[] privateKey;

    private final ProtocolType protocolId;

    private final byte[] publicKey;

    private String delegateIdentityId;

    public IovKeyPair(String identityId, ProtocolType protocolId, byte[] publicKey, byte[] privateKey) {
        this.identityId = identityId;
        this.privateKey = privateKey;
        this.protocolId = protocolId;
        this.publicKey = publicKey;
    }

    public IovKeyPair(String identityId, ProtocolType protocolId, byte[] privateKey, byte[] publicKey, String delegateIdentityId) {
        this(identityId, protocolId, privateKey, publicKey);
        this.delegateIdentityId = delegateIdentityId;
    }

    public IovKeyPair(String identityId, ProtocolType protocolId, KeyPair keyPair) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.privateKey = keyPair.getPrivate().getEncoded();
        this.publicKey = keyPair.getPublic().getEncoded();
    }

    public IovKeyPair(String identityId, ProtocolType protocolId, KeyPair keyPair, String delegateIdentityId) {
        this(identityId, protocolId, keyPair);
        this.delegateIdentityId = delegateIdentityId;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public ProtocolType getProtocolId() {
        return protocolId;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
