package com.iov42.solutions.core.sdk.http;

import com.iov42.solutions.core.sdk.errors.HttpClientException;

import java.util.concurrent.CompletableFuture;

/**
 * Http Client Provider created to meet open-closed principle.
 *
 * @param <R> sync response body type
 */
public interface HttpClientProvider<R> {

    R executeGet(String url, String... headers) throws HttpClientException;

    CompletableFuture<R> executePost(String url, byte[] body, String... headers);

    CompletableFuture<R> executePut(String url, byte[] body, String... headers);
}
