package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.errors.HttpClientError;
import com.iov42.solutions.core.sdk.errors.HttpServerError;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.KeyPairData;
import com.iov42.solutions.core.sdk.model.SignatoryIOV;
import com.iov42.solutions.core.sdk.model.SignatureIOV;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;
import com.iov42.solutions.core.sdk.model.requests.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.utils.HttpUtils;
import com.iov42.solutions.core.sdk.utils.JsonUtils;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public static final String DEFAULT_URL = "https://api.sandbox.iov42.dev/api";

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
     */
    public CompletableFuture<HttpResponse<String>> createIdentity(CreateIdentityRequest request, KeyPairData keyPair) throws Exception {

        SignatoryIOV signatory = new SignatoryIOV(request.getIdentityId(), keyPair.getProtocolId().name(),keyPair.getPrivateKey());

        String body = JsonUtils.toJson(request);

        SignatureIOV authorisationSignature = SecurityUtils.sign(signatory, body);
        SignatureIOV authenticationSignature = SecurityUtils.sign(signatory, authorisationSignature.getSignature());

        List<String> headers = new ArrayList<>();
        headers.add(HttpUtils.AUTHENTICATION);
        headers.add(PlatformUtils.getEncodedHeaderValue(authenticationSignature));
        headers.add(HttpUtils.AUTHORISATIONS);
        headers.add(PlatformUtils.getEncodedHeaderValue(List.of(authorisationSignature)));

        CompletableFuture<HttpResponse<String>> response = HttpUtils.post(url + "/" + version + "/identities", body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));

        return response;
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
    public BaseRequest getRequest(String requestId) throws Exception {
        HttpResponse<String> response = HttpUtils.get(url + "/" + version + "requests/" + requestId);
        return handleResponse(response, BaseRequest.class);
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) throws HttpClientError, HttpServerError {
        if (response.statusCode() >= 400 && response.statusCode() < 500) {
            throw new HttpClientError(response.body(), response.statusCode());
        } else if (response.statusCode() >= 500) {
            throw new HttpServerError(response.body(), response.statusCode());
        }
        return JsonUtils.fromJson(response.body(), clazz);
    }
}
