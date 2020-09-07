package com.iov42.solutions.core.sdk.model.requests.command;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Request structure for transferring assets (unique, accounts or quantities).
 */
@JsonTypeName("TransfersRequest")
public class TransfersRequest extends BaseCommandRequest {

    private final Collection<TransferItem> transfers;

    /**
     * Initializes a new request object.
     * A random request identifier will be used.
     *
     * @param transfers collection of {@link TransferItem}
     */
    public TransfersRequest(List<TransferItem> transfers) {
        this(null, transfers);
    }

    /**
     * Initializes a new request object.
     *
     * @param requestId a specific request identifier
     * @param transfers collection of {@link TransferItem}
     */
    public TransfersRequest(String requestId, Collection<TransferItem> transfers) {
        super(requestId);
        this.transfers = transfers;
    }

    /**
     * Creates a new transfers request object.
     *
     * @param requestId a specific request identifier
     * @param items     collection of {@link TransferItem}
     * @return the {@link TransfersRequest}
     */
    public static TransfersRequest of(String requestId, TransferItem... items) {
        return new TransfersRequest(requestId, Arrays.asList(items));
    }

    /**
     * Creates a new transfers request object.
     * A random request identifier will be used.
     *
     * @param items collection of {@link TransferItem}
     * @return the {@link TransfersRequest}
     */
    public static TransfersRequest of(TransferItem... items) {
        return of(null, items);
    }

    /**
     * Returns a collection of {@link TransferItem}.
     *
     * @return a collection of {@link TransferItem}
     */
    public Collection<TransferItem> getTransfers() {
        return transfers;
    }
}
