package com.iov42.solutions.core.sdk.model;

public class KeyPairData {

    private String identityId;

    private ProtocolType protocolId;

    private String prvKeyBase64;

    private String pubKeyBase64;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public ProtocolType getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(ProtocolType protocolId) {
        this.protocolId = protocolId;
    }

    public String getPrvKeyBase64() {
        return prvKeyBase64;
    }

    public void setPrvKeyBase64(String prvKeyBase64) {
        this.prvKeyBase64 = prvKeyBase64;
    }

    public String getPubKeyBase64() {
        return pubKeyBase64;
    }

    public void setPubKeyBase64(String pubKeyBase64) {
        this.pubKeyBase64 = pubKeyBase64;
    }

    @Override
    public String toString() {
        return "KeyPairData{" +
                "identityId='" + identityId + '\'' +
                ", protocolId=" + protocolId +
                ", prvKeyBase64='" + prvKeyBase64 + '\'' +
                ", pubKeyBase64='" + pubKeyBase64 + '\'' +
                '}';
    }
}
