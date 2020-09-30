package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class TransferRequest extends BaseRequest {

    private final TransferItem[] transfers;

    public TransferRequest(String requestId, TransferItem[] transfers) {
        super(requestId);
        this.transfers = transfers;
    }

    public TransferItem[] getTransfers() {
        return transfers;
    }

}
