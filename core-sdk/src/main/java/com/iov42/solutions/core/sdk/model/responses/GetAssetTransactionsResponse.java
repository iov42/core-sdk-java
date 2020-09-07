package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

/**
 * Structure represents information of transactions for an Asset.
 */
public class GetAssetTransactionsResponse {

    private String next;

    private List<AssetTransactionInfo> transactions;

    private GetAssetTransactionsResponse() {
    }

    GetAssetTransactionsResponse(String next, List<AssetTransactionInfo> transactions) {
        this.next = next;
        this.transactions = transactions;
    }

    /**
     * Returns the entry that the next page will start from.
     * If {@code null}, it means that this was the last page.
     *
     * @return the entry that the next page will start from
     */
    public String getNext() {
        return next;
    }

    /**
     * Returns a {@link List} of transaction objects {@link AssetTransactionInfo}.
     *
     * @return a {@link List} of transaction objects {@link AssetTransactionInfo}
     */
    public List<AssetTransactionInfo> getTransactions() {
        return transactions;
    }
}
