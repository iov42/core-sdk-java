package com.iov42.solutions.core.sdk.model.responses;

public abstract class BaseResponse {

    private String proof;

    public BaseResponse() {
        // needed for Jackson parser
    }

    public BaseResponse(String proof) {
        this.proof = proof;
    }

    public String getProof() {
        return proof;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "proof='" + proof + '\'' +
                '}';
    }
}
