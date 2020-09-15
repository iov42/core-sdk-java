package com.iov42.solutions.core.sdk.model;

import java.security.KeyPair;
import java.util.Arrays;

public class KeyPairWrapper {

    private final String identityId;

    private final byte[] privateKey;

    private final ProtocolType protocolId;

    private final byte[] publicKey;

    public KeyPairWrapper(String identityId, ProtocolType protocolId, KeyPair keyPair) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.privateKey = keyPair.getPrivate().getEncoded();
        this.publicKey = keyPair.getPublic().getEncoded();
    }

    public KeyPairWrapper(String identityId, ProtocolType protocolId, byte[] publicKey, byte[] privateKey) {
        this.identityId = identityId;
        this.privateKey = privateKey;
        this.protocolId = protocolId;
        this.publicKey = publicKey;
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

    @Override
    public String toString() {
        return "KeyPairWrapper{" +
                "identityId='" + identityId + '\'' +
                ", privateKey=" + Arrays.toString(privateKey) +
                ", protocolId=" + protocolId +
                ", publicKey=" + Arrays.toString(publicKey) +
                '}';
    }
}
