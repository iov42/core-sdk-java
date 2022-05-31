package com.iov42.solutions.core.sdk.http;

import java.util.concurrent.CompletableFuture;

/**
 * Http backend used by the {@link com.iov42.solutions.core.sdk.PlatformClient} to communicate with the iov42 platform.
 *
 * Note: when confronted with a redirect, the HttpBackend implementation must handle it.
 */
public interface HttpBackend {

    /**
     * Executes a http request supplied headers and body.
     *
     * @param request the http request
     * @return a {@link HttpBackendResponse}
     */
    CompletableFuture<HttpBackendResponse> execute(HttpBackendRequest request);
}
