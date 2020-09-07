package com.iov42.solutions.core.sdk.model.responses;

import com.iov42.solutions.core.sdk.model.PublicCredentials;

/**
 * Structure represents information of a node.
 */
public class NodeInfoResponse {

    private String nodeId;

    private PublicCredentials publicCredentials;

    private NodeInfoResponse() {
    }

    /**
     * Initialises a new object.
     *
     * @param nodeId            the identifier of the node
     * @param publicCredentials the {@link PublicCredentials} of the node
     */
    public NodeInfoResponse(String nodeId, PublicCredentials publicCredentials) {
        this.nodeId = nodeId;
        this.publicCredentials = publicCredentials;
    }

    /**
     * Returns the identifier of the node.
     *
     * @return the identifier of the node
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Returns the {@link PublicCredentials} of the node.
     *
     * @return the {@link PublicCredentials} of the node
     */
    public PublicCredentials getPublicCredentials() {
        return publicCredentials;
    }
}
