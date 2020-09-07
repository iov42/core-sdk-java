package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Structure for specifying a transfer of ownership of Assets (unique or accounts).
 * Used with the {@link TransfersRequest}.
 */
public class TransferOwnership extends TransferItem {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String assetId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String fromIdentityId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String toIdentityId;

    /**
     * Initializes a new transfer object. This is used to transfer the ownership of a unique Asset or an Account.
     *
     * @param assetTypeId    the AssetType identifier
     * @param assetId        the Asset identifier
     * @param fromIdentityId the sending Identity identifier (must own the unique Asset or Account)
     * @param toIdentityId   the receiving Identity identifier
     */
    public TransferOwnership(String assetTypeId, String assetId, String fromIdentityId, String toIdentityId) {
        super(assetTypeId);
        this.assetId = assetId;
        this.fromIdentityId = fromIdentityId;
        this.toIdentityId = toIdentityId;
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
     * Returns the sending Identity identifier.
     *
     * @return the sending Identity identifier
     */
    public String getFromIdentityId() {
        return fromIdentityId;
    }

    /**
     * Returns the receiving Identity identifier.
     *
     * @return the receiving Identity identifier
     */
    public String getToIdentityId() {
        return toIdentityId;
    }
}
