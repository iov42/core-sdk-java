package com.iov42.solutions.core.sdk.model;


public class SignatureIOV {

    private String delegateIdentityId;

    private String identityId;

    private String protocolId;

    private String signature;

    public SignatureIOV(String identityId, String protocolId, String signature) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.signature = signature;
    }

    public SignatureIOV(String identityId, String delegateIdentityId, String protocolId, String signature) {
        this.identityId = identityId;
        this.delegateIdentityId = delegateIdentityId;
        this.protocolId = protocolId;
        this.signature = signature;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public String getSignature() {
        return signature;
    }
}
