package com.iov42.solutions.core.sdk.model.responses;

public class BaseResponse {

    private String proof;

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "proof='" + proof + '\'' +
                '}';
    }
}
