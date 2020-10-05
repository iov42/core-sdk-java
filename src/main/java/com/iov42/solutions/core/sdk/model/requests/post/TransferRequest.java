package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

import java.util.List;

public class TransferRequest extends BaseRequest {

    private final List<TransferOwnershipItem> transfers;

    public TransferRequest(String requestId, List<TransferOwnershipItem> transfers) {
        super(requestId);
        this.transfers = transfers;
    }

    public List<TransferOwnershipItem> getTransfers() {
        return transfers;
    }
}
