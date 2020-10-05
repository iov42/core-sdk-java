package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

import java.util.List;

public class TransferRequest extends BaseRequest {

    private final List<TransferItem> transfers;

    public TransferRequest(String requestId, List<TransferItem> transfers) {
        super(requestId);
        this.transfers = transfers;
    }

    public List<TransferItem> getTransfers() {
        return transfers;
    }
}
