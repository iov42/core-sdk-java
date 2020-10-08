package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpClientProvider;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.KeyInfo;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.requests.put.*;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.JsonUtils;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import com.iov42.solutions.core.sdk.utils.StringUtils;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The PlatformClient is entry point to access iov42 platform. It is providing ways to
 * create and access identities and to manipulate assets.
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
     * Creates a new asset within the namespace of the given asset-type.
     * An asset is owned by its creator.
     * <p>
     *
     * @param request structure used to create asset, for more details see: {@link CreateAssetRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> createAsset(CreateAssetRequest request, KeyInfo keyPair) {
        String body = JsonUtils.toJson(request);
        List<String> headers = PlatformUtils.createPutHeaders(keyPair, body);

        return executePUT(request.getRequestId(), body, headers);
    }

    /**
     * Creates claims for specified asset.
     * Only the owner identity of the asset can authorise the creation of claims against an asset.
     * <p>
     *
     * @param request structure used to create asset claims, for more details see: {@link CreateAssetClaimsRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> createAssetClaims(CreateAssetClaimsRequest request, KeyInfo keyPair) {
        return createClaims(request, keyPair);
    }

    /**
     * Creates a new asset type
     * An Asset Type is owned by its creator.
     * <p>
     *
     * @param request structure used to create asset type, for more details see: {@link CreateAssetTypeRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> createAssetType(CreateAssetTypeRequest request, KeyInfo keyPair) {

        String body = JsonUtils.toJson(request);
        List<String> headers = PlatformUtils.createPutHeaders(keyPair, body);

        return executePUT(request.getRequestId(), body, headers);
    }

    /**
     * Creates claims for specified asset type.
     * Only the owner identity of the asset-type can authorise the creation of claims against an asset-type.
     * <p>
     *
     * @param request structure used to create asset type claims, for more details see: {@link CreateAssetTypeClaimsRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> createAssetTypeClaims(CreateAssetTypeClaimsRequest request, KeyInfo keyPair) {
        return createClaims(request, keyPair);
    }

    /**
     * Issues a new identity.
     * New client request to be issued a new identity by providing a chosen identifier and a public key that said client has generated.
     * <p>
     *
     * @param request structure used to create new identity, for more details see: {@link CreateIdentityRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> createIdentity(CreateIdentityRequest request, KeyInfo keyPair) {

        String body = JsonUtils.toJson(request);
        List<String> headers = PlatformUtils.createPutHeaders(keyPair, body);

        return executePUT(request.getRequestId(), body, headers);
    }

    /**
     * Creates claims against specified identity.
     * <p>
     *
     * @param request structure used to create identity claims, for more details see: {@link CreateIdentityClaimsRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> createIdentityClaims(CreateIdentityClaimsRequest request, KeyInfo keyPair) {
        return createClaims(request, keyPair);
    }

    /**
     * Create a single atomic request to perform one or more transfers,
     * each consisting of a new ownership or quantity transfer (applicable only to accounts).
     * <p>
     *
     * @param request
     * @param keyPair
     */
    public CompletableFuture<HttpResponse<String>> createTransfer(TransferRequest request, KeyInfo keyPair) {
        String body = JsonUtils.toJson(request);
        List<String> headers = PlatformUtils.createPutHeaders(keyPair, body);

        return executePUT(request.getRequestId(), body, headers);
    }

    /**
     * Endorse claims against an asset
     * <p>
     *
     * @param request structure used to endorse asset claims, for more details see: {@link CreateAssetEndorsementsRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> endorseAssetClaims(CreateAssetEndorsementsRequest request, KeyInfo keyPair) {
        return endorseClaims(request, keyPair);
    }

    /**
     * Endorse claims against an asset type
     * <p>
     *
     * @param request structure used to endorse asset type claims, for more details see: {@link CreateAssetTypeEndorsementsRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> endorseAssetTypeClaims(CreateAssetTypeEndorsementsRequest request, KeyInfo keyPair) {
        return endorseClaims(request, keyPair);
    }

    /**
     * Endorse claims against an identity
     * <p>
     *
     * @param request structure used to endorse identity claims, for more details see: {@link CreateIdentityEndorsementsRequest}
     * @param keyPair iov42 key-pair wrapper, for more details see: {@link KeyInfo}
     * @return {@link CompletableFuture}, when completed, should contain redirect header
     * and so it should be handled with {@link #handleResponse(String requestId, HttpResponse response)}
     */
    public CompletableFuture<HttpResponse<String>> endorseIdentityClaims(CreateIdentityEndorsementsRequest request, KeyInfo keyPair) {
        return endorseClaims(request, keyPair);
    }

    /**
     * Gets an asset instance
     * <p>
     * See the API specs: https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/assets/paths/~1asset-types~1{assetTypeId}~1assets~1{assetId}/get
     *
     * @param request
     * @param keyPair
     * @return
     */
    public GetAssetResponse getAsset(GetAssetRequest request, KeyInfo keyPair) {
        String requestId = request.getRequestId();
        String assetTypeId = request.getAssetTypeId();
        String assetId = request.getAssetId();


        String queryParameters = buildQueryParams(requestId, request.getNodeId());
        String relativeUrl = "/api/" + version + "/asset-types/" + assetTypeId + "/assets/" + assetId + queryParameters;
        String url = this.url + "/" + version + "/asset-types/" + assetTypeId + "/assets/" + assetId + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetAssetResponse.class);
    }

    /**
     * Retrieves information about an asset type
     * <p>
     * For more details, look at the API documentation:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/assets/paths/~1asset-types~1{assetTypeId}/get
     *
     * @param request
     * @param keyPair
     * @return
     */
    public GetAssetTypeResponse getAssetType(GetAssetTypeRequest request, KeyInfo keyPair) {
        String requestId = request.getRequestId();
        String assetTypeId = request.getAssetTypeId();
        String nodeId = request.getNodeId();


        String queryParameters = buildQueryParams(requestId, nodeId);
        String relativeUrl = "/api/" + version + "/asset-types/" + assetTypeId + queryParameters;
        String url = this.url + "/" + version + "/asset-types/" + assetTypeId + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetAssetTypeResponse.class);
    }

    /**
     * Acquires all the claims for the given asset-type
     * <p>
     *
     * @param request {@link GetAssetTypeClaimsRequest}
     * @param keyPair {@link KeyInfo}
     * @return {@link GetClaimsResponse}
     */
    public GetClaimsResponse getAssetTypeClaims(GetAssetTypeClaimsRequest request, KeyInfo keyPair) {
        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        Integer limit = request.getLimit();
        String next = request.getNext();
        String assetTypeId = request.getAssetTypeId();

        String queryParameters = buildQueryParams(requestId, nodeId, limit, next);
        String relativeUrl = "/api/" + version + "/asset-types/" + assetTypeId + "/claims/" + queryParameters;
        String url = this.url + "/" + version + "/asset-types/" + assetTypeId + "/claims/" + queryParameters;

        List<String> headers = PlatformUtils.createGetHeaders(keyPair, relativeUrl);
        HttpResponse<String> response = httpClientProvider.executeGet(url, headers.toArray(new String[0]));

        return handleResponse(response, GetClaimsResponse.class);
    }

    /**
     * Retrieves information about the node's health
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1healthchecks/get
     */
    public Optional<HealthChecks> getHealthChecks() {
        HttpResponse<String> response = httpClientProvider.executeGet(url + "/" + version + "/healthchecks");
        return Optional.ofNullable(handleResponse(response, HealthChecks.class));
    }

    /**
     * Retrieves an identity in the iov42 platform
     * See api spec at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/identities/paths/~1identities~1{identityId}/get
     *
     * @param request parameters
     * @param keyPair -> key pair used to sign the request
     * @return {@link GetIdentityResponse}
     */
    public GetIdentityResponse getIdentity(GetIdentityRequest request, KeyInfo keyPair) {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String identityId = request.getIdentityId();

        String queryParameters = buildQueryParams(requestId, nodeId);
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
    public ClaimEndorsementsResponse getIdentityClaim(GetIdentityClaimRequest request, KeyInfo keyPair) {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String queryParameters = buildQueryParams(requestId, nodeId);
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
     */
    public GetClaimsResponse getIdentityClaims(GetIdentityClaimsRequest request, KeyInfo keyPair) {

        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String queryParameters = buildQueryParams(requestId, nodeId);
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
     */
    public PublicCredentials getIdentityPublicKey(GetIdentityRequest request, KeyInfo keyPair) {
        String requestId = request.getRequestId();
        String nodeId = request.getNodeId();
        String identityId = request.getIdentityId();

        String queryParameters = buildQueryParams(requestId, nodeId);
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
    public Optional<NodeInfoResponse> getNodeInfo() {
        HttpResponse<String> response = httpClientProvider.executeGet(url + "/" + version + "/node-info");
        return Optional.ofNullable(handleResponse(response, NodeInfoResponse.class));
    }

    /**
     * Returns the status of a request in the iov42 platform
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/requests/paths/~1requests~1{requestId}/get
     */
    public RequestInfoResponse getRequest(String requestId) {
        HttpResponse<String> response = httpClientProvider.executeGet(url + "/" + version + "/requests/" + requestId);
        return handleResponse(response, RequestInfoResponse.class);
    }

    /**
     * After each create* request, {@link CompletableFuture} is returned and when complete is should be handled by using this method
     *
     * @param requestId Unique identifier associated with the original request.
     * @param response  {@link HttpResponse}
     * @return Optional {@link BaseResponse}
     */
    public RequestInfoResponse handleResponse(String requestId, HttpResponse<String> response) {
        if (response.statusCode() == 303) {
            return handleRedirect(requestId, response);
        } else {
            return handleResponse(response, RequestInfoResponse.class);
        }
    }

    /**
     * Makes the endorsements map for identity claims.
     * <p>
     *
     * @param endorser    keyPair of the endorser identity.
     * @param subjectId   The subject identifier to which the claims are linked.
     * @param plainClaims plain text list of claims.
     * @return endorsements map
     */
    public Map<String, String> makeEndorsements(KeyInfo endorser, String subjectId, List<String> plainClaims) {
        return plainClaims.stream()
                .collect(Collectors.toMap(
                        PlatformUtils::hashClaim,
                        v -> SecurityUtils.sign(endorser, subjectId + ";" + PlatformUtils.hashClaim(v)).getSignature()));
    }

    /**
     * Makes the endorsements map for asset type or asset claims.
     * <p>
     *
     * @param endorser      keyPair of the endorser identity.
     * @param subjectId     The subject identifier to which the claims are linked.
     * @param subjectTypeId The identifier of the asset-type of the asset against which the claims will be linked.
     * @param plainClaims   plain text list of claims.
     * @return endorsements map.
     */
    public Map<String, String> makeEndorsements(KeyInfo endorser, String subjectId, String subjectTypeId, List<String> plainClaims) {
        return plainClaims.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        v -> SecurityUtils.sign(endorser, subjectId + ";" + subjectTypeId + ";" + PlatformUtils.hashClaim(v)).getSignature()));
    }

    private String buildQueryParams(String requestId, String nodeId) {
        return "?requestId=" + requestId + "&nodeId=" + nodeId;
    }

    private String buildQueryParams(String requestId, String nodeId, Integer limit, String next) {
        StringBuilder builder = new StringBuilder(buildQueryParams(requestId, nodeId));
        if (Objects.nonNull(limit)) {
            builder.append("&limit=");
            builder.append(limit);
        }
        if (StringUtils.isNotEmpty(next)) {
            builder.append("&next=");
            builder.append(next);
        }
        return builder.toString();
    }

    private CompletableFuture<HttpResponse<String>> createClaims(CreateClaimsRequest request, KeyInfo keyPair) {
        List<String> plainClaims = request.getClaims();
        List<String> encodedClaims = PlatformUtils.hashClaims(plainClaims.stream());
        request.setClaims(encodedClaims);

        String body = JsonUtils.toJson(request);
        List<String> headers = PlatformUtils.createClaimsHeaders(keyPair, body, plainClaims);

        return executePUT(request.getRequestId(), body, headers);
    }

    private CompletableFuture<HttpResponse<String>> endorseClaims(CreateEndorsementsRequest request, KeyInfo keyPair) {
        String body = JsonUtils.toJson(request);
        List<String> headers = PlatformUtils.createEndorsementsHeaders(keyPair, body);

        return executePUT(request.getRequestId(), body, headers);
    }

    private CompletableFuture<HttpResponse<String>> executePUT(String requestId, String body, List<String> headers) {
        return httpClientProvider.executePut(url + "/" + version + "/requests/" + requestId, body.getBytes(StandardCharsets.UTF_8), headers.toArray(new String[0]));
    }

    private RequestInfoResponse handleRedirect(String requestId, HttpResponse<String> response) {
        HttpHeaders headers = response.headers();
        Optional<String> optLocation = headers.firstValue("location");
        int reTryAfter = Integer.parseInt(headers.firstValue("retry-after").orElse("5"));

        if (optLocation.isPresent()) {
            try {
                /* location if present = /api/v1/requests/{requestId} */
                return PlatformUtils.waitForRequest(requestId, this, reTryAfter);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return null;
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) {
        return JsonUtils.fromJson(response.body(), clazz);
    }
}
