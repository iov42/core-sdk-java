package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackendException;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;
import com.iov42.solutions.core.sdk.http.spi.HttpBackend;
import com.iov42.solutions.core.sdk.model.PlatformError;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.SignatoryInfo;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import com.iov42.solutions.core.sdk.utils.SafeLazyValueHolder;
import com.iov42.solutions.core.sdk.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

class HttpHostQueryExecutor implements PlatformQueryExecutor {

    private static final Logger log = LoggerFactory.getLogger(HttpHostQueryExecutor.class);

    public static class Builder {

        private final HttpHostWrapper httpHostWrapper;
        private final SafeLazyValueHolder<String> nodeIdHolder = new SafeLazyValueHolder<>(this::fetchNodeId, Objects::nonNull);

        public Builder(HttpHostWrapper httpHostWrapper) {
            this.httpHostWrapper = httpHostWrapper;
        }

        public PlatformQueryExecutor build(SignatoryInfo authenticatorInfo) {
            return new HttpHostQueryExecutor(httpHostWrapper, nodeIdHolder, authenticatorInfo);
        }

        private String fetchNodeId() {
            NodeInfoResponse nodeInfoResponse = httpHostWrapper.executeGet("/api/v1/node-info", null, NodeInfoResponse.class);
            log.debug("Fetching new node id...");
            if (nodeInfoResponse == null) {
                throw new HttpBackendException("Error fetching new node id!");
            }
            log.debug("New node id is {}", nodeInfoResponse.getNodeId());
            return nodeInfoResponse.getNodeId();
        }
    }

    private static class PlainRequest extends BaseRequest {
    }

    private final HttpHostWrapper httpHostWrapper;
    private final SafeLazyValueHolder<String> nodeIdHolder;
    private final SignatoryInfo authenticatorInfo;

    private static final int MAX_GET_RETRY = 2;

    /**
     * Initializes a new executor object.
     *
     * @param httpHostWrapper   the wrapped {@link HttpBackend} to be used
     * @param nodeIdHolder      the holder for the node identifier to be used
     * @param authenticatorInfo {@link SignatoryInfo} used to authenticate the request
     */
    public HttpHostQueryExecutor(HttpHostWrapper httpHostWrapper, SafeLazyValueHolder<String> nodeIdHolder, SignatoryInfo authenticatorInfo) {
        this.httpHostWrapper = httpHostWrapper;
        this.nodeIdHolder = nodeIdHolder;
        this.authenticatorInfo = authenticatorInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAssetResponse getAsset(GetAssetRequest request) {
        String assetTypeId = request.getAssetTypeId();
        String assetId = request.getAssetId();

        String path = "/api/v1/asset-types/" + assetTypeId + "/assets/" + assetId + buildQueryParameters(request);
        return executeGet(path, GetAssetResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClaimEndorsementsResponse getAssetClaim(GetAssetClaimRequest request) {
        String assetTypeId = request.getAssetTypeId();
        String assetId = request.getAssetId();
        String claim = request.getHashedClaim();

        String path = "/api/v1/asset-types/" + assetTypeId + "/assets/" + assetId + "/claims/" + claim + buildQueryParameters(request);
        return executeGet(path, ClaimEndorsementsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetClaimsResponse getAssetClaims(GetAssetClaimsRequest request) {
        String assetTypeId = request.getAssetTypeId();
        String assetId = request.getAssetId();

        String path = "/api/v1/asset-types/" + assetTypeId + "/assets/" + assetId + "/claims" + buildQueryParameters(request);
        return executeGet(path, GetClaimsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAssetTransactionsResponse getAssetTransactions(GetAssetTransactionsRequest request) {
        String assetTypeId = request.getAssetTypeId();
        String assetId = request.getAssetId();

        String path = "/api/v1/asset-types/" + assetTypeId + "/assets/" + assetId + "/transactions" + buildQueryParameters(request);
        return executeGet(path, GetAssetTransactionsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAssetTypeResponse getAssetType(GetAssetTypeRequest request) {
        String assetTypeId = request.getAssetTypeId();

        String path = "/api/v1/asset-types/" + assetTypeId + buildQueryParameters(request);
        return executeGet(path, GetAssetTypeResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetClaimsResponse getAssetTypeClaims(GetAssetTypeClaimsRequest request) {
        String assetTypeId = request.getAssetTypeId();

        String path = "/api/v1/asset-types/" + assetTypeId + "/claims/" + buildQueryParameters(request);
        return executeGet(path, GetClaimsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClaimEndorsementsResponse getAssetTypeClaim(GetAssetTypeClaimRequest request) {
        String assetTypeId = request.getAssetTypeId();
        String claim = request.getHashedClaim();

        String path = "/api/v1/asset-types/" + assetTypeId + "/claims/" + claim + buildQueryParameters(request);
        return executeGet(path, ClaimEndorsementsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProofInfoResponse getProofs(String requestId) {
        String path = "/api/v1/proofs/" + requestId + buildQueryParameters(new PlainRequest());
        return executeGet(path, ProofInfoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetIdentityResponse getIdentity(GetIdentityRequest request) {
        String identityId = request.getIdentityId();

        String path = "/api/v1/identities/" + identityId + buildQueryParameters(request);
        return executeGet(path, GetIdentityResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClaimEndorsementsResponse getIdentityClaim(GetIdentityClaimRequest request) {
        String identityId = request.getIdentityId();
        String hashedClaim = request.getHashedClaim();

        String path = "/api/v1/identities/" + identityId + "/claims/" + hashedClaim + buildQueryParameters(request);
        return executeGet(path, ClaimEndorsementsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetClaimsResponse getIdentityClaims(GetIdentityClaimsRequest request) {
        String identityId = request.getIdentityId();

        String path = "/api/v1/identities/" + identityId + "/claims" + buildQueryParameters(request);
        return executeGet(path, GetClaimsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublicCredentials getIdentityPublicKey(GetIdentityRequest request) {
        String identityId = request.getIdentityId();

        String path = "/api/v1/identities/" + identityId + "/public-key" + buildQueryParameters(request);
        return executeGet(path, PublicCredentials.class);
    }

    private String buildQueryParameters(BaseRequest request) {
        String query = "?requestId=" + request.getRequestId();
        if (request instanceof PageableRequest) {

            PageableRequest pageableRequest = (PageableRequest) request;
            StringBuilder builder = new StringBuilder(query);
            if (Objects.nonNull(pageableRequest.getLimit())) {
                builder.append("&limit=");
                builder.append(pageableRequest.getLimit());
            }
            if (StringUtils.isNotEmpty(pageableRequest.getNext())) {
                builder.append("&next=");
                builder.append(pageableRequest.getNext());
            }
            return builder.toString();
        }
        return query;
    }

    private <T> T executeGet(String path, Class<T> responseClass) {
        return executeGet(path, responseClass, 0);
    }

    private <T> T executeGet(String path, Class<T> responseClass, int retry) {

        if (retry >= MAX_GET_RETRY) {
            throw new HttpBackendException("Reached retry limit for get requests.");
        }

        // add the node id to the path
        String finalPath = path + "&nodeId=" + nodeIdHolder.getValue();

        // build the headers
        Map<String, List<String>> headers = PlatformUtils.createGetHeaders(authenticatorInfo, finalPath);

        // perform request
        HttpBackendResponse backendResponse = httpHostWrapper.executeGet(finalPath, headers, HttpBackendResponse.class);

        // check for client or server errors
        if (!backendResponse.isSuccess()) {
            ErrorResponse errorResponse = HttpHostWrapper.convertResponse(backendResponse, ErrorResponse.class);
            if (errorResponse != null && errorResponse.getErrors() != null) {
                for (PlatformError error : errorResponse.getErrors()) {
                    if (error.getErrorCode() == 1901) {
                        // the node id was invalid - try to retrieve a new one and try again
                        nodeIdHolder.invalidate();
                        return executeGet(path, responseClass, retry + 1);
                    }
                }
            }
        }
        return HttpHostWrapper.convertResponse(backendResponse, responseClass);
    }
}
