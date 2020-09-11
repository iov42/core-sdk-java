package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.PublicCredentials;

public class NodeInfoResponse {

    private String nodeId;

    private PublicCredentials publicCredentials;

    public NodeInfoResponse(String nodeId, PublicCredentials publicCredentials) {
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
