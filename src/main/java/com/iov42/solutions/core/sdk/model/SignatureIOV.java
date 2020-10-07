package com.iov42.solutions.core.sdk.model;


import com.fasterxml.jackson.annotation.JsonInclude;

public class SignatureIOV {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String delegateIdentityId;

    private final String identityId;

    private final String protocolId;

    private final String signature;

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
