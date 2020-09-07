package com.iov42.solutions.core.sdk.http;

import java.util.List;
import java.util.Map;

/**
 * Response structure of a HTTP request.
 */
public class HttpBackendResponse {

    private final String requestUrl;

    private final Map<String, List<String>> headers;

    private final int statusCode;

    private final String body;

    /**
     * Initializes a new instance of the {@link HttpBackendResponse}.
     *
     * @param requestUrl the URL of the request
     * @param headers    the {@link Map} of response headers
     * @param statusCode the response status code
     * @param body       the response body
     */
    public HttpBackendResponse(String requestUrl, Map<String, List<String>> headers, int statusCode, String body) {
        this.requestUrl = requestUrl;
        this.headers = headers;
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * Returns the headers of the response.
     *
     * @return the headers of the response
     */
    public Map<String, List<String>> headers() {
        return headers;
    }

    /**
     * Returns the HTTP status code of the response.
     *
     * @return the HTTP status code of the response
     */
    public int statusCode() {
        return statusCode;
    }

    /**
     * Returns the body of the response as {@code String}.
     *
     * @return the body of the response.
     */
    public String body() {
        return body;
    }

    /**
     * Returns {@code True} if the response status code is less than 400.
     *
     * @return {@code True} if the response status code is less than 400
     */
    public boolean isSuccess() {
        return statusCode < 400;
    }

    /**
     * Returns the URL of the request.
     *
     * @return the URL of the request
     */
    public String getRequestUrl() {
        return requestUrl;
    }
}
