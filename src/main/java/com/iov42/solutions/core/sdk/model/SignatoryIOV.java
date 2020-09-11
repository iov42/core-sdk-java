package com.iov42.solutions.core.sdk.model;


public class SignatoryIOV {

    private String delegatorIdentityId;

    private String identityId;

    private byte[] privateKey;

    private String protocolId;

    public SignatoryIOV(String identityId, String protocolId, byte[] privateKey) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.privateKey = privateKey;
    }

    public SignatoryIOV(String delegatorIdentityId, String identityId, String protocolId, byte[] privateKey) {
        this(identityId, protocolId, privateKey);
        this.delegatorIdentityId = delegatorIdentityId;
    }

    public String getDelegatorIdentityId() {
        return delegatorIdentityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public String getProtocolId() {
        return protocolId;
    }
}
