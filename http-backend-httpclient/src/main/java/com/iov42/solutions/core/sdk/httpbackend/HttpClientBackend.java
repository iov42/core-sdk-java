package com.iov42.solutions.core.sdk.httpbackend;

import com.iov42.solutions.core.sdk.http.HttpBackendException;
import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 *  Implementation of a {@link HttpBackend} to be used by the {@link com.iov42.solutions.core.sdk.PlatformClient}
 *
 *  Uses the {@link HttpClient} from Java9 as underlying http client.
 */
public class HttpClientBackend implements HttpBackend {

    private final HttpClient httpClient;

    public HttpClientBackend() {
        this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    }

    public HttpClientBackend(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpBackendResponse executeGet(String url, Collection<String> headers) throws HttpBackendException {
        try {
            HttpRequest request = build(url, headers).GET().build();
            return convert(request.uri().toString(), httpClient.send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (Exception e) {
            throw new HttpBackendException(String.format("Failed to execute GET request: [%s]", url), e);
        }
    }

    @Override
    public CompletableFuture<HttpBackendResponse> executePut(String url, byte[] body, Collection<String> headers) throws HttpBackendException {
        try {
            HttpRequest request = build(url, headers).PUT(HttpRequest.BodyPublishers.ofByteArray(body)).build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(r -> convert(request.uri().toString(), r));
        } catch (Exception e) {
            throw new HttpBackendException(String.format("Failed to execute async POST request: [%s]", url), e);
        }
    }

    private HttpRequest.Builder build(String url, Collection<String> headers) throws URISyntaxException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(url));
        if (hasHeaders(headers)) {
            builder = builder.headers(headers.toArray(String[]::new));
        }
        return builder;
    }

    private boolean hasHeaders(Collection<String> headers) {
        return Objects.nonNull(headers) && headers.size() > 0;
    }

    private HttpBackendResponse convert(String url, HttpResponse<String> response) {
        return new HttpBackendResponse(url, response.headers().map(), response.statusCode(), response.body());
    }
}
