package com.iov42.solutions.core.sdk.model.responses;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Structure represents Asset transaction information.
 */
public class AssetTransactionInfo {

    private String requestId;

    private BigInteger quantity;

    private LocalDateTime transactionTimestamp;

    private String proof;

    private AssetInfo sender;

    private AssetInfo recipient;

    private AssetTransactionInfo() {
    }

    AssetTransactionInfo(String requestId, AssetInfo sender, AssetInfo recipient, BigInteger quantity, LocalDateTime transactionTimestamp, String proof) {
        this.requestId = requestId;
        this.quantity = quantity;
        this.transactionTimestamp = transactionTimestamp;
        this.proof = proof;
        this.sender = sender;
        this.recipient = recipient;
    }

    /**
     * Returns the used request identifier.
     *
     * @return the used request identifier
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns the transferred quantity or {@code null} if not applicable.
     *
     * @return the transferred quantity or {@code null} if not applicable
     */
    public BigInteger getQuantity() {
        return quantity;
    }

    /**
     * Returns the timestamp of transfer (in UTC).
     *
     * @return the timestamp of transfer (in UTC)
     */
    public LocalDateTime getTransactionTimestamp() {
        return transactionTimestamp;
    }

    /**
     * Returns a link to the proof of the transfer.
     *
     * @return a link to the proof of the transfer
     */
    public String getProof() {
        return proof;
    }

    /**
     * Returns an {@link AssetInfo} of the sender.
     *
     * @return an {@link AssetInfo} of the sender
     */
    public AssetInfo getSender() {
        return sender;
    }

    /**
     * Returns an {@link AssetInfo} of the recipient.
     *
     * @return an {@link AssetInfo} of the recipient
     */
    public AssetInfo getRecipient() {
        return recipient;
    }
}
