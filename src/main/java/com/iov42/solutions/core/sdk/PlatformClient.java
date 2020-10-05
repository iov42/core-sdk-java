package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.errors.PlatformException;
import com.iov42.solutions.core.sdk.http.HttpClientProvider;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.requests.post.*;
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
     * Creates a new asset
     * <p>
     * See the API specs at: https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/assets/paths/~1asset-types~1{assetTypeId}~1assets/post
     *
     * @param request
     * @param keyPair
     * @return
     */
    public CompletableFuture<HttpResponse<String>> createAsset(BaseCreateAssetRequest request, IovKeyPair keyPair) {
        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createPostHeaders(keyPair, body);

        String url = this.url + "/" + version + "/asset-types/" + request.getAssetTypeId() + "/assets";
        return httpClientProvider.executePost(url, body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    /**
     * Creates a new asset type
     * <p>
     * See the api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/assets/paths/~1asset-types/post
     *
     * @param request
     * @param keyPair
     * @return
     */
    public CompletableFuture<HttpResponse<String>> createAssetType(CreateAssetTypeRequest request, IovKeyPair keyPair) {
        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createPostHeaders(keyPair, body);

        String url = this.url + "/" + version + "/asset-types";
        return httpClientProvider.executePost(url, body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
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

        List<String> headers = PlatformUtils.createPostClaimsHeaders(keyPair, body, plainClaims);

        return httpClientProvider.executePost(url + "/" + version + "/identities/" + request.getSubjectId() + "/claims", body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    /**
     * Endorses identity's claims
     *
     * @param request
     * @param keyPair
     * @return {@link java.util.concurrent.CompletableFuture}
     * @see <a href="https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}~1endorsements/post">Endorse claims against an identity</a>
     */
    public CompletableFuture<HttpResponse<String>> endorseIdentityClaims(CreateEndorsementsRequest request, IovKeyPair keyPair) {
        String subjectId = request.getSubjectId();

        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createPostEndorsementsHeaders(keyPair, body);

        String url = this.url + "/" + version + "/identities/" + subjectId + "/endorsements";
        return httpClientProvider.executePost(url, body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    /**
     * Gets an asset instance
     * <p>
     * See the API specs: https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/assets/paths/~1asset-types~1{assetTypeId}~1assets~1{assetId}/get
     *
     * @param request
     * @param keyPair
     * @return
     * @throws PlatformException
     */
    public GetAssetResponse getAsset(GetAssetRequest request, IovKeyPair keyPair) throws PlatformException {
        String requestId = request.getRequestId();
        String assetTypeId = request.getAssetTypeId();
        String assetId = request.getAssetId();


        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, request.getNodeId());
        String relativeUrl = "/api/" + version + "/asset-types/" + assetTypeId + "/assets/" + assetId + queryParameters;
        String url = this.url + "/" + version + "/asset-types/" + assetTypeId + "/assets/" + assetId + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetAssetResponse.class);
    }

    /**
     * Reads information about an asset type
     * <p>
     * See the API specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/assets/paths/~1asset-types~1{assetTypeId}/get
     *
     * @param request
     * @param keyPair
     * @return
     */
    public GetAssetTypeResponse getAssetType(GetAssetTypeRequest request, IovKeyPair keyPair) throws PlatformException {
        String requestId = request.getRequestId();
        String assetTypeId = request.getAssetTypeId();
        String nodeId = request.getNodeId();


        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, nodeId);
        String relativeUrl = "/api/" + version + "/asset-types/" + assetTypeId + queryParameters;
        String url = this.url + "/" + version + "/asset-types/" + assetTypeId + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetAssetTypeResponse.class);
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
     *
     * @param request parameters
     * @param keyPair -> key pair used to sign the request
     * @return {@link GetIdentityResponse}
     * @throws Exception
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
     *
     * @param request parameters
     * @param keyPair -> key pair used to sign the request
     * @return {@link ClaimEndorsementsResponse}
     * @throws Exception
     */
    public ClaimEndorsementsResponse getIdentityClaim(GetIdentityClaimRequest request, IovKeyPair keyPair) throws Exception {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, nodeId);
        String identityId = request.getIdentityId();
        String hashedClaim = request.getHashedClaim();

        String relativeUrl = "/api/" + version + "/identities/" + identityId + "/claims/" + hashedClaim + queryParameters;
        String url = this.url + "/" + version + "/identities/" + identityId + "/claims/" + hashedClaim + queryParameters;

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
     * Retrieves identity public credentials, like public key and protocol
     * See the api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}~1public-key/get
     *
     * @param request parameters
     * @param keyPair -> key pair used to sign the request
     * @return {@link PublicCredentials}
     * @throws Exception
     */
    public PublicCredentials getIdentityPublicKey(GetIdentityRequest request, IovKeyPair keyPair) throws Exception {
        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String identityId = request.getIdentityId();

        String queryParameters = String.format("?requestId=%s&nodeId=%s", requestId, nodeId);
        String relativeUrl = "/api/" + version + "/identities/" + identityId + "/public-key" + queryParameters;
        String url = this.url + "/" + version + "/identities/" + identityId + "/public-key" + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));
        return handleResponse(response, PublicCredentials.class);
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
     * After a POST request, IOV42 Platform returns HTTP 303 which you can handle by using this method
     *
     * @param requestId
     * @param response
     * @return Optional {@link RequestInfoResponse}
     */
    public Optional<RequestInfoResponse> handleRedirect(String requestId, HttpResponse<String> response) {
        HttpHeaders headers = response.headers();
        Optional<String> optLocation = headers.firstValue("location");
        int reTryAfter = Integer.parseInt(headers.firstValue("retry-after").orElse("5"));

        if (optLocation.isPresent()) {
            try {
                RequestInfoResponse info = PlatformUtils.waitForRequest(requestId, this, reTryAfter);
                /* location if present = /api/v1/requests/{requestId} */
                return Optional.of(info);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return Optional.empty();
    }

    public <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) throws PlatformException {
        if (response.statusCode() >= 400) {
            throw new PlatformException(response.body());
        } else if (Objects.isNull(response.body()) || response.body().trim().length() == 0) {
            return null;
        }
        return JsonUtils.fromJson(response.body(), clazz);
    }

    /**
     * Create a single atomic request to perform one or more transfers, each consisting of a new ownership or quantity transfer (applicable only to accounts).
     * <p>
     * See the API specs: https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/transfers/paths/~1transfers/post
     *
     * @param request
     * @param keyPair
     */
    public CompletableFuture<HttpResponse<String>> transfer(TransferRequest request, IovKeyPair keyPair) {
        String body = JsonUtils.toJson(request);

        List<String> headers = PlatformUtils.createPostHeaders(keyPair, body);

        return httpClientProvider.executePost(url + "/" + version + "/transfers", body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));

    }
}
