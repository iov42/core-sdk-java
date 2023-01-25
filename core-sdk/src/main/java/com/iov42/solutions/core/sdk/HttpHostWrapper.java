package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.spi.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendException;
import com.iov42.solutions.core.sdk.http.HttpBackendRequest;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;
import com.iov42.solutions.core.sdk.model.PlatformErrorException;
import com.iov42.solutions.core.sdk.model.responses.ErrorResponse;
import com.iov42.solutions.core.sdk.utils.serialization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

class HttpHostWrapper implements HttpBackend {

    private static final Logger log = LoggerFactory.getLogger(HttpHostWrapper.class);

    private static final Duration RETRY_TIMEOUT = Duration.ofSeconds(40);
    private final HttpBackend httpBackend;
    private final String platformHost;

    public HttpHostWrapper(HttpBackend httpBackend, String platformHost) {
        this.httpBackend = httpBackend;
        this.platformHost = platformHost;
    }

    public <T> T executeGet(String url, Map<String, List<String>> headers, Class<T> responseClass) {
        try {
            return execute(Instant.now(), new HttpBackendRequest(HttpBackendRequest.Method.GET, url, headers, null), responseClass).join();
        } catch (CompletionException ex) {
            if (ex.getCause() instanceof RuntimeException) {
                throw (RuntimeException) ex.getCause();
            }
            throw new HttpBackendException(ex.getCause());
        }
    }

    public <T> CompletableFuture<T> executePut(String url, byte[] body, Map<String, List<String>> headers, Class<T> responseClass) {
        return execute(Instant.now(), new HttpBackendRequest(HttpBackendRequest.Method.PUT, url, headers, body), responseClass);
    }

    @Override
    public CompletableFuture<HttpBackendResponse> execute(HttpBackendRequest request) {
        log.debug("Http {} {}", request.getMethod(), request.getRequestUrl());

        HttpBackendRequest requestClone = new HttpBackendRequest(request.getMethod(),
                platformHost + request.getRequestUrl(),
                request.getHeaders(), request.getBody());

        return httpBackend.execute(requestClone).thenApply(r -> r != null ? r.mutate(request) : null);
    }

    private <T> CompletableFuture<T> execute(Instant start, HttpBackendRequest request, Class<T> responseClass) throws HttpBackendException {
        return execute(request).thenCompose(r -> handleResponse(start, r, responseClass));
    }

    private <T> CompletableFuture<T> handleResponse(Instant start, HttpBackendResponse response, Class<T> responseClass) {
        int statusCode = response.statusCode();
        if (statusCode >= 300 && statusCode <= 399) {
            // we are asked to redirect
            Duration waited = Duration.between(start, Instant.now());
            if (waited.compareTo(RETRY_TIMEOUT) < 0) {

                String location = getValue(response.headers().get("Location"));
                String retryAfter = getValue(response.headers().get("Retry-After"));
                if (retryAfter != null) {
                    long delayMillis = Long.parseLong(retryAfter) * 1000;
                    try {
                        Thread.sleep(delayMillis);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // build redirect request and execute
                HttpBackendRequest redirectRequest = response.getRequest().mutate(location);
                log.trace("Retrying request {}", response.getRequest().getRequestUrl());
                return execute(start, redirectRequest, responseClass);
            } else {
                throw new PlatformRetryExceededException();
            }
        }
        return CompletableFuture.completedFuture(convertResponse(response, responseClass));
    }

    public static <T> T convertResponse(HttpBackendResponse response, Class<T> clazz) {
        if (HttpBackendResponse.class.isAssignableFrom(clazz)) {
            // no conversion here just return the response
            return (T) response;
        }

        int statusCode = response.statusCode();
        if (!response.isSuccess() && !clazz.isAssignableFrom(ErrorResponse.class)) {
            // response is an error response
            if (log.isErrorEnabled()) {
                log.error("Request failed! Url: {}, Status: {}, Body: {}", response.getRequest().getRequestUrl(), response.statusCode(), response.body());
            }
            ErrorResponse errorResponse = convertResponse(response, ErrorResponse.class);
            throw new PlatformErrorException(statusCode, errorResponse);
        }
        if (log.isDebugEnabled()) {
            log.debug("Http Response. Url: {}, Status: {}, Body: {}", response.getRequest().getRequestUrl(), response.statusCode(), response.body());
        }
        return JsonUtils.fromJson(response.body(), clazz);
    }

    private static String getValue(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }
}