package com.iov42.solutions.core.sdk.model.requests.get;

/**
 * A request that supports paging.
 */
public interface PageableRequest {

    /**
     * Returns the page limit or {@code null}.
     * The number of results to be returned in the response of this request.
     * This is an optional parameter and if not supplied, the number of results will default to 20.
     *
     * @return the page limit or {@code null}
     */
    Integer getLimit();

    /**
     * Returns the next value or {@code null}.
     * The next entry (inclusive) from which the results in the page will start.
     * This is an optional parameter and if not supplied the results will start from the very first entry.
     *
     * @return the next value or {@code null}.
     */
    String getNext();
}
