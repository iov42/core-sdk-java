package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.errors.PlatformError;
import com.iov42.solutions.core.sdk.errors.PlatformException;
import com.iov42.solutions.core.sdk.http.HttpClientProvider;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.requests.*;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.JsonUtils;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The PlatformClient is entry point to access iov42 platform. It is providing ways to:
 * <p>
 * Create and access identities
 * Manipulate assets
 */
public class PlatformClient {

    private final HttpClientProvider<HttpResponse<String>> httpClientProvider;

    private String url;

    private String version = "v1";

    public PlatformClient(String url, HttpClientProvider<HttpResponse<String>> httpClientProvider) {
        if (Objects.isNull(url) || url.trim().length() == 0) {
            throw new IllegalArgumentException("Invalid URL argument");
        }
        this.url = url;
        if (this.url.endsWith("/")) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        this.httpClientProvider = httpClientProvider;
    }

    public PlatformClient(String url, String version, HttpClientProvider<HttpResponse<String>> httpClientProvider) {
        this(url, httpClientProvider);
        this.version = version;
    }

    /**
     * Creates an identity in the iov42 platform
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities/post
     * Input:
     * request -> request details
     * keyPair -> key pair used to sign the request
     *
     * @return CompletableFuture<HttpResponse < String>> response
     */
    public CompletableFuture<HttpResponse<String>> createIdentity(CreateIdentityRequest request, IovKeyPair keyPair) {

        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createPostHeaders(keyPair, body);

        return httpClientProvider.executePost(url + "/" + version + "/identities", body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    /**
     * Creates identity's claims
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}~1claims/post
     * Input:
     * request -> request details
     * claims -> array of claims, encoded as strings
     * keyPair -> key pair used to sign the request
     * delegatorIdentityId -> identity on which behalf the request is signed, if different than the one in the keyPair
     */
    public CompletableFuture<HttpResponse<String>> createIdentityClaims(CreateClaimsRequest request, IovKeyPair keyPair) {
        List<String> plainClaims = request.getClaims();
        List<String> encodedClaims = plainClaims.stream().map(PlatformUtils::getEncodedClaimHash).collect(Collectors.toList());
        request.setClaims(encodedClaims);

        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createPostHeadersWithClaims(keyPair, body, plainClaims);

        return httpClientProvider.executePost(url + "/" + version + "/identities/" + request.getSubjectId() + "/claims", body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    /**
     * Retrieves information about the node's health
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1healthchecks/get
     */
    public Optional<HealthChecks> getHealthChecks() throws Exception {
        HttpResponse<String> response = httpClientProvider.executeGet(url + "/" + version + "/healthchecks");
        return Optional.of(handleResponse(response, HealthChecks.class));
    }

    /**
     * Retrieves an identity in the iov42 platform
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}/get
     * Input:
     * identityId -> identity's identifier
     * keyPair -> key pair used to sign the request
     * delegatorIdentityId -> identity on which behalf the request is executed, if different than the one in the keyPair
     */
    public GetIdentityResponse getIdentity(GetIdentityRequest request, IovKeyPair keyPair) throws Exception {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String identityId = request.getIdentityId();

        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, nodeId);
        String relativeUrl = "/api/" + version + "/identities/" + identityId + queryParameters;
        String url = this.url + "/" + version + "/identities/" + identityId + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetIdentityResponse.class);
    }

    /**
     * Retrieves a single identity's claim
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}~1claims~1{hashedClaim}/get
     * Input:
     * identityId -> identity's identifier
     * keyPair -> key pair used to sign the request
     * delegatorIdentityId -> identity on which behalf the request is signed, if different than the one in the keyPair
     */
    public ClaimEndorsementsResponse getIdentityClaim(GetIdentityClaimRequest request, IovKeyPair keyPair) throws Exception {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, nodeId);
        String identityId = request.getIdentityId();
        String hashedClaim = request.getHashedClaim();

        String relativeUrl = "/api/" + version + "/identities/" + identityId + "/claims/" + hashedClaim + queryParameters;
        String url = this.url + "/" + version + "/identities/" + identityId + "/claims" + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, ClaimEndorsementsResponse.class);
    }

    /**
     * Retrieves an identity's claims
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}~1claims/get
     *
     * @param request parameters
     * @param keyPair -> key pair used to sign the request
     * @return {@link GetClaimsResponse}
     * @throws Exception
     */
    public GetClaimsResponse getIdentityClaims(GetIdentityClaimsRequest request, IovKeyPair keyPair) throws Exception {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, nodeId);
        String identityId = request.getIdentityId();

        String relativeUrl = "/api/" + version + "/identities/" + identityId + "/claims" + queryParameters;
        String url = this.url + "/" + version + "/identities/" + identityId + "/claims" + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetClaimsResponse.class);
    }

    /**
     * Retrieves information about a node in the iov42 platform
     * See the api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1node-info/get
     */
    public Optional<NodeInfoResponse> getNodeInfo() throws Exception {
        HttpResponse<String> response = httpClientProvider.executeGet(url + "/" + version + "/node-info");
        return Optional.of(handleResponse(response, NodeInfoResponse.class));
    }

    /**
     * Returns the status of a request in the iov42 platform
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/requests/paths/~1requests~1{requestId}/get
     */
    public RequestInfoResponse getRequest(String requestId) throws Exception {
        HttpResponse<String> response = httpClientProvider.executeGet(url + "/" + version + "/requests/" + requestId);
        return handleResponse(response, RequestInfoResponse.class);
    }

    /**
     * After a POST request, IOV42 returns HTTP 303 which you can handle by using this method
     *
     * @param requestId
     * @param headers
     * @return Optional {@link RequestInfoResponse}
     */
    public Optional<RequestInfoResponse> handleRedirect(String requestId, HttpHeaders headers) {
        Optional<String> optLocation = headers.firstValue("location");
        int reTryAfter = Integer.parseInt(headers.firstValue("retry-after").orElse("5"));

        if (optLocation.isPresent()) {
            try {
                RequestInfoResponse info = PlatformUtils.waitForRequest(requestId, this, reTryAfter);
                /* location if present = /api/v1/requests/{requestId} */
                return Optional.of(info);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    public <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) throws PlatformException {
        if (response.statusCode() >= 400) {
            PlatformError error = JsonUtils.fromJson(response.body(), PlatformError.class);
            throw new PlatformException(error.toString());
        }
        return JsonUtils.fromJson(response.body(), clazz);
    }
}
