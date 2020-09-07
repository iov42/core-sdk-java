package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.math.BigInteger;

/**
 * Request structure for creating a new Asset (account or unique).
 */
@JsonTypeName("CreateAssetRequest")
public class CreateAssetRequest extends BaseCommandRequest {

    private final String assetId;

    private final String assetTypeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final BigInteger quantity;

    /**
     * Initializes a new request object. This variant is for an unique asset.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     */
    public CreateAssetRequest(String assetTypeId, String assetId) {
        this(null, assetTypeId, assetId, null);
    }

    /**
     * Initializes a new request object. This variant is for an unique asset.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     */
    public CreateAssetRequest(String requestId, String assetTypeId, String assetId) {
        this(requestId, assetTypeId, assetId, null);
    }

    /**
     * Initializes a new request object. This variant is for an account.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     * @param quantity    the initial balance (only valid for accounts NOT unique Assets)
     */
    public CreateAssetRequest(String assetTypeId, String assetId, BigInteger quantity) {
        this(null, assetTypeId, assetId, quantity);
    }

    /**
     * Initializes a new request object. This variant is for an account.
     * A random request identifier will be used.
     *
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     * @param quantity    the initial balance (only valid for accounts NOT unique Assets)
     */
    public CreateAssetRequest(String assetTypeId, String assetId, long quantity) {
        this(assetTypeId, assetId, BigInteger.valueOf(quantity));
    }

    /**
     * Initializes a new request object. This variant is for an account.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     * @param quantity    the initial balance (only valid for accounts NOT unique Assets)
     */
    public CreateAssetRequest(String requestId, String assetTypeId, String assetId, BigInteger quantity) {
        super(requestId);
        this.assetTypeId = assetTypeId;
        this.assetId = assetId;
        this.quantity = quantity;
    }

    /**
     * Initializes a new request object. This variant is for an account.
     *
     * @param requestId   a specific request identifier
     * @param assetTypeId the AssetType identifier
     * @param assetId     the Asset identifier
     * @param quantity    the initial balance (only valid for accounts NOT unique Assets)
     */
    public CreateAssetRequest(String requestId, String assetTypeId, String assetId, long quantity) {
        this(requestId, assetTypeId, assetId, BigInteger.valueOf(quantity));
    }

    /**
     * Returns the Asset identifier.
     *
     * @return the Asset identifier
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * Returns the AssetType identifier.
     *
     * @return the AssetType identifier
     */
    public String getAssetTypeId() {
        return assetTypeId;
    }

    /**
     * Returns the quantity (initial balance for accounts).
     *
     * @return the quantity (initial balance for accounts)
     */
    public BigInteger getQuantity() {
        return quantity;
    }
}
