package com.iov42.solutions.core.sdk.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class HttpClientUtil {

    private HttpClientUtil() {
        // static usage only
    }

    public static HttpResponse<String> get(String url, String... headers) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = build(url, headers).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> post(String url, String body, String... headers) throws URISyntaxException {
        HttpRequest request = build(url, headers).POST(HttpRequest.BodyPublishers.ofString(body)).build();
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
