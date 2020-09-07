package com.iov42.solutions.core.sdk.model;

public class AuthorisationData {

    private String delegateIdentityId;

    private String identityId;

    private String protocolId;

    private String signature;

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public void setDelegateIdentityId(String delegateIdentityId) {
        this.delegateIdentityId = delegateIdentityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "AuthorisationData{" +
                "delegateIdentityId='" + delegateIdentityId + '\'' +
                ", identityId='" + identityId + '\'' +
                ", protocolId='" + protocolId + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
