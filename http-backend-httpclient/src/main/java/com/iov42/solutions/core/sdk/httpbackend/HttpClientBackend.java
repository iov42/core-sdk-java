package com.iov42.solutions.core.sdk.httpbackend;

import com.iov42.solutions.core.sdk.http.HttpBackendException;
import com.iov42.solutions.core.sdk.http.spi.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendRequest;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *  Implementation of a {@link HttpBackend} to be used by the {@link com.iov42.solutions.core.sdk.PlatformClient}
 *
 *  Uses the {@link HttpClient} from Java9 as underlying http client.
 */
public class HttpClientBackend implements HttpBackend {

    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(40);

    private final HttpClient httpClient;

    public HttpClientBackend() {
        this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    }

    public HttpClientBackend(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public CompletableFuture<HttpBackendResponse> execute(HttpBackendRequest request) {
        try {
            var bodyPublisher = request.getBody() != null
                    ? HttpRequest.BodyPublishers.ofByteArray(request.getBody())
                    : HttpRequest.BodyPublishers.noBody();

            var httpRequest = build(request.getRequestUrl(), request.getHeaders())
                    .method(request.getMethod().name(), bodyPublisher)
                    .timeout(DEFAULT_REQUEST_TIMEOUT)
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(r -> convertResponse(request, r));
        } catch (Exception ex) {
            throw new HttpBackendException(String.format("Failed to execute async request: [%s]", request.getRequestUrl()), ex);
        }
    }

    private HttpRequest.Builder build(String url, Map<String, List<String>> headers) throws URISyntaxException {
        var builder = HttpRequest.newBuilder(new URI(url));
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, values) -> values.forEach(value -> builder.header(key, value)));
        }
        return builder;
    }

    private HttpBackendResponse convertResponse(HttpBackendRequest request, HttpResponse<String> httpResponse) {
        return new HttpBackendResponse(request, httpResponse.headers().map(), httpResponse.statusCode(), httpResponse.body());
    }
}
