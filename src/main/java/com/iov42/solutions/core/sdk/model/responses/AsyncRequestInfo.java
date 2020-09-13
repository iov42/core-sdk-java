package com.iov42.solutions.core.sdk.model.responses;

public class AsyncRequestInfo {

    private String requestId;

    private String proof;

    private String[] resources;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String[] getResources() {
        return resources;
    }

    public void setResources(String[] resources) {
        this.resources = resources;
    }

    public boolean hasFinished() {
        return resources != null;
    }
}
