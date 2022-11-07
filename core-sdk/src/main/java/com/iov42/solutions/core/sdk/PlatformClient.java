package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendRequest;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.SignatoryInfo;
import com.iov42.solutions.core.sdk.model.requests.command.*;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The {@link PlatformClient} is the entry point to access iov42 platform. It is providing ways to
 * create and access identities and to manipulate assets.
 *
 * <p>See detailed API specification here:
 * <a href="https://tech.iov42.com/platform/api/">iov42 platform API specification</a>
 */
public class PlatformClient {

    private static final Logger log = LoggerFactory.getLogger(PlatformClient.class);

    private final HttpHostWrapper httpHostWrapper;

    private final HttpHostQueryExecutor.Builder queryExecutorBuilder;

    /**
     * Initializes a new {@link PlatformClient} instance.
     *
     * @param platformHostUrl the host url
     * @param httpBackend     the underlying {@link HttpBackend} used to issue http requests
     */
    public PlatformClient(String platformHostUrl, HttpBackend httpBackend) {
        if (Objects.isNull(platformHostUrl) || platformHostUrl.trim().length() == 0) {
            throw new IllegalArgumentException("Invalid URL argument");
        }
        if (platformHostUrl.endsWith("/")) {
            platformHostUrl = platformHostUrl.substring(0, platformHostUrl.length() - 1);
        }
        this.httpHostWrapper = new HttpHostWrapper(httpBackend, platformHostUrl);
        this.queryExecutorBuilder = new HttpHostQueryExecutor.Builder(httpHostWrapper);
    }

    /**
     * Initializes a new {@link PlatformClient} instance.
     * Internally uses the Java {@link ServiceLoader} to get an instance of a {@link HttpBackend}.
     *
     * @param platformHostUrl the host url
     */
    public PlatformClient(String platformHostUrl) {
        this(platformHostUrl, loadHttpBackendService());
    }

    private static HttpBackend loadHttpBackendService() {
        ServiceLoader<HttpBackend> loader = ServiceLoader.load(HttpBackend.class);
        Iterator<HttpBackend> it = loader.iterator();
        if (!it.hasNext()) {
            throw new IllegalStateException("Could not find a registered HttpBackend service. Use a different constructor.");
        }
        HttpBackend candidate = it.next();
        if (it.hasNext()) {
            log.warn("Warning! More than one HttpBackend implementation found. Taking first!");
        }
        log.info("Using {} instance as HttpBackend!", candidate.getClass().getCanonicalName());
        return candidate;
    }

    /**
     * Returns a {@link PlatformQueryExecutor} to query the iov42 platform where each request is authenticated with the
     * provided {@link SignatoryInfo}.
     *
     * @param authenticatorInfo {@link com.iov42.solutions.core.sdk.model.SignatureInfo} to authenticate requests
     * @return a {@link PlatformQueryExecutor} instance
     */
    public PlatformQueryExecutor queryAs(SignatoryInfo authenticatorInfo) {
        return queryExecutorBuilder.build(authenticatorInfo);
    }

    private static void addExtendedHeaders(List<String> headers, AuthorisedRequest authorisedRequest) {

        // if claims header check is required and authorisations less than 2 modify claims header
        if (authorisedRequest.isRequireClaimHeaderCheck() && authorisedRequest.getAuthorisations().size() < 2) {
            int i = 0;
            String key = "";
            for (String h : authorisedRequest.getHeaders()) {
                if (i++ == 0) {
                    // it is a key
                    key = h;
                } else {
                    i = 0;
                    if (Objects.equals(key, Constants.HEADER_IOV42_CLAIMS)) {
                        h = Constants.ENDORSER_ONLY_CLAIM_HEADER_VALUE;
                    }
                    headers.add(key);
                    headers.add(h);
                }
            }
        } else {
            // just add all the headers
            headers.addAll(authorisedRequest.getHeaders());
        }
    }

    /**
     * Sends an {@link AuthorisedRequest} authenticated with the supplied {@link SignatoryInfo} to the platform.
     *
     * @param authorisedRequest  the {@link AuthorisedRequest}
     * @param authenticationInfo the {@link SignatoryInfo} used for authentication
     * @return a {@link CompletableFuture} of a {@link RequestInfoResponse}
     * @see <a href="https://tech.iov42.com/platform/api/#tag/requests">iov42 platform API specification: requests</a>
     */
    public CompletableFuture<RequestInfoResponse> send(AuthorisedRequest authorisedRequest, SignatoryInfo authenticationInfo) {
        List<String> headers = PlatformUtils.createPutHeaders(authenticationInfo, authorisedRequest.getAuthorisations());

        // check additional custom request headers
        if (authorisedRequest.getHeaders() != null && !authorisedRequest.getHeaders().isEmpty()) {
            addExtendedHeaders(headers, authorisedRequest);
        }
        return executeCommand(authorisedRequest.getRequestId(), authorisedRequest.getBody(), headers);
    }

    /**
     * Sends a {@link BaseCommandRequest} authorised and authenticated with the supplied {@link SignatoryInfo} to the platform.
     *
     * @param request       the {@link BaseCommandRequest}
     * @param signatoryInfo the {@link SignatoryInfo} used for authorisation and authentication
     * @return a {@link CompletableFuture} of a {@link RequestInfoResponse}
     * @see <a href="https://tech.iov42.com/platform/api/#tag/requests">iov42 platform API specification: requests</a>
     */
    public CompletableFuture<RequestInfoResponse> send(BaseCommandRequest request, SignatoryInfo signatoryInfo) {
        AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request).authorise(signatoryInfo);
        return send(authorisedRequest, signatoryInfo);
    }

    /**
     * Gets health check information.
     *
     * @return an optional {@link HealthChecks}
     * @see <a href="https://tech.iov42.com/platform/api/#tag/operations/paths/~1healthchecks/get">iov42 platform API specification: health check</a>
     */
    public Optional<HealthChecks> getHealthChecks() {
        return Optional.ofNullable(httpHostWrapper.executeGet("/api/v1/healthchecks", null, HealthChecks.class));
    }

    /**
     * Gets information of an API node of the platform.
     *
     * @return an optional {@link NodeInfoResponse}
     * @see <a href="https://tech.iov42.com/platform/api/#tag/operations/paths/~1node-info/get">iov42 platform API specification: node info</a>
     */
    public Optional<NodeInfoResponse> getNodeInfo() {
        return Optional.ofNullable(httpHostWrapper.executeGet("/api/v1/node-info", null, NodeInfoResponse.class));
    }

    /**
     * Gets information about a specific request.
     *
     * @param requestId the request identifier
     * @return a {@link RequestInfoResponse}
     */
    public RequestInfoResponse getRequest(String requestId) {
        return httpHostWrapper.executeGet("/api/v1/requests/" + requestId, null, RequestInfoResponse.class);
    }

    private CompletableFuture<RequestInfoResponse> executeCommand(String requestId, String body, List<String> headers) {
        return httpHostWrapper.executePut("/api/v1/requests/" + requestId, body.getBytes(StandardCharsets.UTF_8),
                HttpBackendRequest.buildHeaderMap(headers), RequestInfoResponse.class);
    }
}
