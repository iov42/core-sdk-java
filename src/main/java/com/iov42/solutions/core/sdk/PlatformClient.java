package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.errors.HttpClientError;
import com.iov42.solutions.core.sdk.errors.HttpServerError;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.KeyPairWrapper;
import com.iov42.solutions.core.sdk.model.requests.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.AsyncRequestInfo;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.utils.HttpUtils;
import com.iov42.solutions.core.sdk.utils.JsonUtils;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The PlatformClient is entry point to access iov42 platform. It is providing ways to:
 * <p>
 * Create and access identities
 * Manipulate assets
 */
public class PlatformClient {

    public static final String DEFAULT_URL = "https://api.sandbox.iov42.dev";

    private String url;

    private String version = "v1";

    public PlatformClient(String url) {
        if (Objects.isNull(url) || url.trim().length() == 0) {
            throw new IllegalArgumentException("Invalid URL argument");
        }
        this.url = url;
        if (this.url.endsWith("/")) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
    }

    public PlatformClient(String url, String version) {
        this(url);
        this.version = version;
    }

    /**
     * Creates an identity in the iov42 platform
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities/post
     * Input:
     * request -> request details
     * keyPair -> key pair used to sign the request
     * @return
     */
    public CompletableFuture<HttpResponse<String>> createIdentity(CreateIdentityRequest request, KeyPairWrapper keyPair) throws Exception {

        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createHeaders(keyPair, body);

        return HttpUtils.postAsync(url + "/" + version + "/identities", body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    /**
     * Retrieves information about the node's health
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1healthchecks/get
     */
    public Optional<HealthChecks> getHealthChecks() throws Exception {
        HttpResponse<String> response = HttpUtils.get(url + "/" + version + "/healthchecks");
        return Optional.of(handleResponse(response, HealthChecks.class));
    }

    /**
     * Retrieves information about a node in the iov42 platform
     * See the api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1node-info/get
     */
    public Optional<NodeInfoResponse> getNodeInfo() throws Exception {
        HttpResponse<String> response = HttpUtils.get(url + "/" + version + "/node-info");
        return Optional.of(handleResponse(response, NodeInfoResponse.class));
    }

    /**
     * Returns the status of a request in the iov42 platform
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/requests/paths/~1requests~1{requestId}/get
     */
    public AsyncRequestInfo getRequest(String requestId) throws Exception {
        HttpResponse<String> response = HttpUtils.get(url + "/" + version + "requests/" + requestId);
        return handleResponse(response, AsyncRequestInfo.class);
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) throws HttpClientError, HttpServerError {
        if (response.statusCode() >= 400 && response.statusCode() < 500) {
            throw new HttpClientError(response.body(), response.statusCode());
        } else if (response.statusCode() >= 500) {
            throw new HttpServerError(response.body(), response.statusCode());
        }
        return JsonUtils.fromJson(response.body(), clazz);
    }

    private void handleResponse(HttpResponse<String> response) throws HttpClientError, HttpServerError {
        if (response.statusCode() == 303) {

        } else if (response.statusCode() >= 400 && response.statusCode() < 500) {
            throw new HttpClientError(response.body(), response.statusCode());
        } else if (response.statusCode() >= 500) {
            throw new HttpServerError(response.body(), response.statusCode());
        }
    }
}
