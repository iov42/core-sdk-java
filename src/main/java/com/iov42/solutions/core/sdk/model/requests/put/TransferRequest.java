package com.iov42.solutions.core.sdk.model.requests.put;

import java.util.List;

public class TransferRequest extends BasePutRequest {

    private final List<TransferOwnershipItem> transfers;

    public TransferRequest(List<TransferOwnershipItem> transfers) {
        super(TransactionType.TransfersRequest);
        this.transfers = transfers;
    }

    public TransferRequest(String requestId, List<TransferOwnershipItem> transfers) {
        super(requestId, TransactionType.TransfersRequest);
        this.transfers = transfers;
    }

    public List<TransferOwnershipItem> getTransfers() {
        return transfers;
    }
}
