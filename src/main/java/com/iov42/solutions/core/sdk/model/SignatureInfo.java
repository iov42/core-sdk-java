package com.iov42.solutions.core.sdk.model;


import com.fasterxml.jackson.annotation.JsonInclude;

public class SignatureInfo {

    private final String identityId;

    private final ProtocolType protocolId;

    private final String signature;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String delegateIdentityId;

    public SignatureInfo(String identityId, ProtocolType protocolId, String signature) {
        this.identityId = identityId;
        this.protocolId = protocolId;
        this.signature = signature;
    }

    public SignatureInfo(String identityId, String delegateIdentityId, ProtocolType protocolId, String signature) {
        this(identityId, protocolId, signature);
        this.delegateIdentityId = delegateIdentityId;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public ProtocolType getProtocolId() {
        return protocolId;
    }

    public String getSignature() {
        return signature;
    }
}
