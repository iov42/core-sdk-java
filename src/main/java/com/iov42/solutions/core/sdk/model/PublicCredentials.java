package com.iov42.solutions.core.sdk.model;

public class PublicCredentials {

    private String key;

    private String protocolId;

    private PublicCredentials() {
    }

    public PublicCredentials(String protocolId, String key) {
        this.key = key;
        this.protocolId = protocolId;
    }

    public String getKey() {
        return key;
    }

    public String getProtocolId() {
        return protocolId;
    }

    @Override
    public String toString() {
        return "PublicCredentials{" +
                "key='" + key + '\'' +
                ", protocolId='" + protocolId + '\'' +
                '}';
    }
}
