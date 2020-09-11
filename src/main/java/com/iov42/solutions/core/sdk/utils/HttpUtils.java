package com.iov42.solutions.core.sdk.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class HttpUtils {

    public static final String AUTHORISATIONS = "X-IOV42-Authorisations";

    public static final String AUTHENTICATION = "X-IOV42-Authentication";

    public static HttpResponse<String> get(String url, String... headers) throws URISyntaxException, IOException, InterruptedException {
        var request = build(url, headers).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> post(String url, byte[] body, String... headers) throws URISyntaxException {
        var request = build(url, headers).POST(HttpRequest.BodyPublishers.ofByteArray(body)).build();
        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> put(String url, String body, String... headers) throws URISyntaxException {
        var request = build(url, headers).PUT(HttpRequest.BodyPublishers.ofString(body)).build();
        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpRequest.Builder build(String url, String... headers) throws URISyntaxException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
        if (hasHeaders(headers)) {
            builder = builder.headers(headers);
        }
        return builder;
    }

    private static boolean hasHeaders(String[] headers) {
        return Objects.nonNull(headers) && headers.length > 0;
    }
}
