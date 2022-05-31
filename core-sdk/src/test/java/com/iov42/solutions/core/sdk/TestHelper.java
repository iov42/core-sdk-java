package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackendRequest;

import static org.mockito.ArgumentMatchers.argThat;

class TestHelper {

    private TestHelper() {}

    public static HttpBackendRequest get(String requestUrl) {
        return argThat(arg -> arg != null && arg.getMethod() == HttpBackendRequest.Method.GET && requestUrl.equals(arg.getRequestUrl()));
    }

    public static HttpBackendRequest put(String requestUrl) {
        return argThat(arg -> arg != null && arg.getMethod() == HttpBackendRequest.Method.PUT && requestUrl.equals(arg.getRequestUrl()));
    }
}
