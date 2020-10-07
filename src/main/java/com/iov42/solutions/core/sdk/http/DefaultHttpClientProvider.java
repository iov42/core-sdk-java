package com.iov42.solutions.core.sdk.http;

import com.iov42.solutions.core.sdk.errors.HttpClientException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DefaultHttpClientProvider implements HttpClientProvider<HttpResponse<String>> {

    private final HttpClient httpClient;

    public DefaultHttpClientProvider() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public DefaultHttpClientProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse<String> executeGet(String url, String... headers) throws HttpClientException {
        try {
            HttpRequest request = build(url, headers).GET().build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new HttpClientException(String.format("Failed to execute GET request: [%s]", url), e);
        }
    }

    @Override
    public CompletableFuture<HttpResponse<String>> executePut(String url, byte[] body, String... headers) {
        try {
            HttpRequest request = build(url, headers).PUT(HttpRequest.BodyPublishers.ofByteArray(body)).build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new HttpClientException(String.format("Failed to execute async POST request: [%s]", url), e);
        }
    }

    private HttpRequest.Builder build(String url, String... headers) throws URISyntaxException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
        if (hasHeaders(headers)) {
            builder = builder.headers(headers);
        }
        return builder;
    }

    private boolean hasHeaders(String... headers) {
        return Objects.nonNull(headers) && headers.length > 0;
    }
}
