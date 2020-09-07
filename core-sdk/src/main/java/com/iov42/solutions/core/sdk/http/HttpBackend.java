package com.iov42.solutions.core.sdk.http;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Http backend used by the {@link com.iov42.solutions.core.sdk.PlatformClient} to communicate with the iov42 platform.
 *
 * Note: when confronted with a redirect, the HttpBackend implementation must handle it.
 */
public interface HttpBackend {

    /**
     * Executes a http get for a given url with supplied headers.
     * The supplied headers {@code String} instances must alternate as header names and header values.
     * To add several values to the same name then the same name must be supplied with each new value.
     *
     * @param url     the request url
     * @param headers the list of name value pairs
     * @return a {@link HttpBackendResponse}
     * @throws HttpBackendException
     */
    HttpBackendResponse executeGet(String url, Collection<String> headers) throws HttpBackendException;

    /**
     * Executes a http put for a given url with supplied headers and body.
     * The supplied headers {@code String} instances must alternate as header names and header values.
     * To add several values to the same name then the same name must be supplied with each new value.
     *
     * @param url     the request url
     * @param body    the request body
     * @param headers the list of name value pairs
     * @return a {@link HttpBackendResponse}
     */
    CompletableFuture<HttpBackendResponse> executePut(String url, byte[] body, Collection<String> headers) throws HttpBackendException;
}
