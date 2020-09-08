package com.iov42.solutions.core.sdk.model;

public class NodeInfo {

    private String nodeId;

    private PublicCredentials publicCredentials;

    public NodeInfo(String nodeId, PublicCredentials publicCredentials) {
        this.nodeId = nodeId;
        this.publicCredentials = publicCredentials;
    }

    public String getNodeId() {
        return nodeId;
    }

    public PublicCredentials getPublicCredentials() {
        return publicCredentials;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "nodeId='" + nodeId + '\'' +
                ", publicCredentials=" + publicCredentials +
                '}';
    }
}
