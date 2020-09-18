package com.iov42.solutions.core.sdk.model.responses;

public class BaseResponse {

    private final String proof;

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
