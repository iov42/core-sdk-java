package com.iov42.solutions.core.sdk.model.responses;

/**
 * Base response structure.
 */
public abstract class BaseResponse {

    private String proof;

    protected BaseResponse() {
    }

    protected BaseResponse(String proof) {
        this.proof = proof;
    }

    /**
     * Returns the latest successful proof url that brought the asset to its current state.
     *
     * @return the latest successful proof url that brought the asset to its current state
     */
    public String getProof() {
        return proof;
    }
}
