package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendException;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;
import com.iov42.solutions.core.sdk.model.PlatformErrorException;
import com.iov42.solutions.core.sdk.model.responses.ErrorResponse;
import com.iov42.solutions.core.sdk.utils.serialization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class HttpHostWrapper implements HttpBackend {

    private static final Logger log = LoggerFactory.getLogger(HttpHostWrapper.class);

    private static final int MAX_RETRY_COUNT = 5;

    private final HttpBackend httpBackend;
    private final String platformHost;

    public HttpHostWrapper(HttpBackend httpBackend, String platformHost) {
        this.httpBackend = httpBackend;
        this.platformHost = platformHost;
    }

    public <T> T executeGet(String url, Collection<String> headers, Class<T> responseClass) throws HttpBackendException {
        return handleResponse(executeGet(url, headers), responseClass);
    }

    @Override
    public HttpBackendResponse executeGet(String url, Collection<String> headers) throws HttpBackendException {
        log.debug("GET {}", url);
        // TODO provide async option for get request
        return httpBackend.executeGet(platformHost + url, headers);
    }

    public <T> CompletableFuture<T> executePut(String url, byte[] body, Collection<String> headers, Class<T> responseClass) throws HttpBackendException {
        return executePut(url, body, headers)
                .thenApply(r -> handleResponse(r, responseClass));
    }

    @Override
    public CompletableFuture<HttpBackendResponse> executePut(String url, byte[] body, Collection<String> headers) throws HttpBackendException {
        log.debug("PUT {}", url);
        return httpBackend.executePut(platformHost + url, body, headers);
    }

    private <T> T handleResponse(HttpBackendResponse response, Class<T> responseClass) {
        int retryCounter = MAX_RETRY_COUNT;
        while (retryCounter-- > 0) {
            int statusCode = response.statusCode();
            if (statusCode >= 300 && statusCode <= 399) {
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
                response = executeGet(location, null);
                continue;
            }
            break;
        }
        return convertResponse(response, responseClass);
    }

    public static <T> T convertResponse(HttpBackendResponse response, Class<T> clazz) {
        int statusCode = response.statusCode();
        if (!response.isSuccess() && !clazz.isAssignableFrom(ErrorResponse.class)) {
            // response is an error response
            log.error("Request failed! Url: {}, Status: {}, Body: {}", response.getRequestUrl(), response.statusCode(), response.body());
            ErrorResponse errorResponse = convertResponse(response, ErrorResponse.class);
            throw new PlatformErrorException(statusCode, errorResponse);
        }
        return JsonUtils.fromJson(response.body(), clazz);
    }

    private static String getValue(List<String> values) {
        if (values == null || values.size() == 0){
            return null;
        }
        return values.get(0);
    }
}