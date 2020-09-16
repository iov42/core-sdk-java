package com.iov42.solutions.core.sdk.model.responses;

/**
 * Response object of the {@link com.iov42.solutions.core.sdk.PlatformClient#getRequest(String)}
 */
public class RequestInfoResponse {

    private String proof;

    private String requestId;

    private String[] resources;

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
