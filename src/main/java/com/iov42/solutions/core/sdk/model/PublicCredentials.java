package com.iov42.solutions.core.sdk.model;

public class PublicCredentials {

    private String key;

    private String protocolId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    @Override
    public String toString() {
        return "PublicCredentials{" +
                "key='" + key + '\'' +
                ", protocolId='" + protocolId + '\'' +
                '}';
    }
}
