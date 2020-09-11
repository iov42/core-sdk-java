package com.iov42.solutions.core.sdk.model;

import java.util.Arrays;

public class KeyPairData {

    private final String identityId;

    private final byte[] privateKey;

    private final ProtocolType protocolId;

    private final byte[] publicKey;

    public KeyPairData(String identityId, ProtocolType protocolId, byte[] publicKey, byte[] privateKey) {
        this.identityId = identityId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.protocolId = protocolId;
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
        return "KeyPairData{" +
                "identityId='" + identityId + '\'' +
                ", privateKey=" + Arrays.toString(privateKey) +
                ", protocolId=" + protocolId +
                ", publicKey=" + Arrays.toString(publicKey) +
                '}';
    }
}
