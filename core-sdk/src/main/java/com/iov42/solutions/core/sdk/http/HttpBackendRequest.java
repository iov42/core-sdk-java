package com.iov42.solutions.core.sdk.http;

import java.util.*;

/**
 * Request structure of a HTTP request.
 */
public final class HttpBackendRequest {

    /**
     * Enum representing supported HTTP methods.
     */
    public enum Method {
        GET,
        PUT
    }

    private final Method method;

    private final String requestUrl;

    private final Map<String, List<String>> headers;

    private final byte[] body;

    /**
     * Initializes a new instance of the {@link HttpBackendRequest} (without body).
     *
     * @param method     the method
     * @param headers    the {@link Map} of request headers
     * @param requestUrl the request url
     */
    public HttpBackendRequest(Method method, String requestUrl, Map<String, List<String>> headers) {
        this(method, requestUrl, headers, null);
    }

    /**
     * Initializes a new instance of the {@link HttpBackendRequest}.
     * The supplied headers {@code String} instances must alternate as header names and header values.
     * To add several values to the same name then the same name must be supplied with each new value.
     *
     * @param method     the method
     * @param headers    the {@link Collection} of request headers
     * @param requestUrl the request url
     * @param body       the request body
     */
    public HttpBackendRequest(Method method, String requestUrl, Collection<String> headers, byte[] body) {
        this(method, requestUrl, buildHeaderMap(headers), body);
    }

    /**
     * Initializes a new instance of the {@link HttpBackendRequest}.
     *
     * @param method     the method
     * @param headers    the {@link Map} of request headers
     * @param requestUrl the request url
     * @param body       the request body
     */
    public HttpBackendRequest(Method method, String requestUrl, Map<String, List<String>> headers, byte[] body) {
        this.method = method;
        this.requestUrl = requestUrl;
        this.headers = headers;
        this.body = body;
    }

    /**
     * Returns the request method.
     *
     * @return the request method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns the request url.
     *
     * @return the request url.
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * Returns the request headers.
     *
     * @return the request headers.
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Returns the request body (can be null if there is no body).
     *
     * @return the request body.
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Creates a shallow clone of the request and mutates the request url.
     *
     * @return the cloned request.
     */
    public HttpBackendRequest mutate(String requestUrl) {
        return new HttpBackendRequest(method, requestUrl, headers, body);
    }

    /**
     * Creates a header map from a list of headers.
     * The supplied headers {@code String} instances must alternate as header names and header values.
     * To add several values to the same name then the same name must be supplied with each new value.
     *
     * @return the header map.
     */
    public static Map<String, List<String>> buildHeaderMap(Collection<String> headers) {
        Map<String, List<String>> headerMap = new HashMap<>();
        if (headers != null) {
            if (headers.size() % 2 != 0) {
                throw new IllegalArgumentException("Wrong number of header values");
            }
            int i = 0;
            String name = null;
            for (String headerVal : headers) {
                if (i++ == 0) {
                    name = headerVal;
                } else {
                    headerMap.computeIfAbsent(name, n -> new ArrayList<>()).add(headerVal);
                    i = 0;
                }
            }
        }
        return headerMap;
    }
}
