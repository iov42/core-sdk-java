package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigInteger;

/**
 * Structure for specifying a transfer of quantity from one Account to another.
 * Used with the {@link TransfersRequest}.
 */
public class TransferQuantity extends TransferItem {

    private final String fromAssetId;

    private final String toAssetId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final BigInteger quantity;

    /**
     * Initializes a new transfer object. This is used to transfer the quantities from one Asset (account) to another.
     *
     * @param assetType   the AssetType identifier
     * @param fromAssetId the sending Asset (Account) identifier
     * @param toAssetId   the receiving Asset(Account) identifier
     * @param quantity    the quantity to be transferred
     */
    public TransferQuantity(String assetType, String fromAssetId, String toAssetId, BigInteger quantity) {
        super(assetType);
        this.fromAssetId = fromAssetId;
        this.toAssetId = toAssetId;
        this.quantity = quantity;
    }

    /**
     * Initializes a new transfer object. This is used to transfer the quantities from one Asset (account) to another.
     *
     * @param assetType   the AssetType identifier
     * @param fromAssetId the sending Asset (Account) identifier
     * @param toAssetId   the receiving Asset(Account) identifier
     * @param quantity    the quantity to be transferred
     */
    public TransferQuantity(String assetType, String fromAssetId, String toAssetId, long quantity) {
        this(assetType, fromAssetId, toAssetId, BigInteger.valueOf(quantity));
    }

    /**
     * Returns the sending Asset (Account) identifier.
     *
     * @return the sending Asset (Account) identifier
     */
    public String getFromAssetId() {
        return fromAssetId;
    }

    /**
     * Returns the receiving Asset (Account) identifier.
     *
     * @return the receiving Asset (Account) identifier
     */
    public String getToAssetId() {
        return toAssetId;
    }

    /**
     * Returns the quantity to be transferred.
     *
     * @return the quantity to be transferred
     */
    public BigInteger getQuantity() {
        return quantity;
    }
}
