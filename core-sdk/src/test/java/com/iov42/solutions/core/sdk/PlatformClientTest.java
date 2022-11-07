package com.iov42.solutions.core.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendRequest;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.command.AuthorisedRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateAssetTypeRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateIdentityClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateIdentityEndorsementsRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.Constants;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlatformClientTest {

    private static final String PLATFORM_HOST_URL = "http://test.mock";

    private PlatformClient platformClient;

    private HttpBackend httpBackend;

    @BeforeEach
    public void before() {
        httpBackend = mock(HttpBackend.class);
        platformClient = new PlatformClient(PLATFORM_HOST_URL, httpBackend);
    }

    private static String buildRequestInfoResponse(String requestId, String... resources) {
        return String.format("{\"requestId\":\"%s\",\"resources\":[%s],\"proof\":\"/api/v1/proofs/%s\"}",
                requestId,
                Arrays.stream(resources).map(s -> "\"" + s + "\"").collect(Collectors.joining(",")),
                requestId
        );
    }

    private static CompletableFuture<HttpBackendResponse> response(int statusCode, Object body) {
        String bodyStr;
        if (body instanceof String) {
            bodyStr = (String) body;
        } else {
            try {
                bodyStr = new ObjectMapper().writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("UnitTest: Could not serialize JSON body.");
            }
        }
        return CompletableFuture.completedFuture(new HttpBackendResponse(null,null, statusCode, bodyStr));
    }

    private static CompletableFuture<HttpBackendResponse> responseOk(Object body) {
        return response(200, body);
    }

    private static CompletableFuture<HttpBackendResponse> commandResponse(String requestId, String... resources) {
        return responseOk(buildRequestInfoResponse(requestId, resources));
    }

    private static void assertSingleResource(RequestInfoResponse response, String expectedResource) {
        assertEquals(1, response.getResources().size());
        assertEquals(expectedResource, response.getResources().get(0));
    }

    private void whenExecuteCommand(String requestId, String resource) {
        when(httpBackend.execute(TestHelper.put(PLATFORM_HOST_URL + "/api/v1/requests/" + requestId)))
                .thenReturn(commandResponse(requestId, resource));
    }

    @SafeVarargs
    private final <T> ArrayList<T> list(T... elements) {
        ArrayList<T> result = new ArrayList<T>();
        Collections.addAll(result, elements);
        return result;
    }

    @Test
    void commandRequestShouldBeSuccessful() {

        // prepare
        String requestId = randomId();
        String assetTypeId = randomId();

        whenExecuteCommand(requestId, "/api/v1/asset-types/" + assetTypeId);

        // act
        CreateAssetTypeRequest request = new CreateAssetTypeRequest(requestId, assetTypeId);
        RequestInfoResponse response = platformClient.send(request, randomSignatoryInfo())
                .join();

        // assert
        assertTrue(response.hasFinished());
    }

    @Test
    void authorisedRequestShouldBeSuccessful() {

        // prepare
        String requestId = randomId();
        String assetTypeId = randomId();
        SignatoryInfo actor = randomSignatoryInfo();

        whenExecuteCommand(requestId, "/api/v1/asset-types/" + assetTypeId);

        // act
        CreateAssetTypeRequest request = new CreateAssetTypeRequest(requestId, assetTypeId);
        AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request).authorise(actor);

        RequestInfoResponse response = platformClient.send(authorisedRequest, actor)
                .join();

        // assert
        assertTrue(response.hasFinished());
    }

    @Test
    void additionalHeadersShouldBeAdded() {

        // prepare
        String identityId = "1234";
        Claims claims = Claims.of("claim1");
        SignatoryInfo actor = randomSignatoryInfo();

        List<String> headers = new ArrayList<>();
        when(httpBackend.execute(any()))
                .then(invocation -> {
                    HttpBackendRequest req = invocation.getArgument(0, HttpBackendRequest.class);
                    headers.addAll(req.getHeaders().keySet());
                    return CompletableFuture.completedFuture(responseOk(""));
                });

        // act
        CreateIdentityClaimsRequest request = new CreateIdentityClaimsRequest(identityId, claims);
        AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request).authorise(actor);
        platformClient.send(authorisedRequest, actor);

        // assert
        assertTrue(headers.contains(Constants.HEADER_IOV42_CLAIMS));
    }

    @Test
    void additionalHeadersShouldBeCorrectedAndAdded() {
        // in case of an endorsement without claim creation the claims header must be corrected

        // prepare
        String identityId = "1234";
        Claims claims = Claims.of("claim1");
        SignatoryInfo actor = randomSignatoryInfo();

        Endorsements endorsements = new Endorsements(actor, identityId, claims);

        List<String> headers = new ArrayList<>();
        when(httpBackend.execute(any()))
                .then(invocation -> {
                    HttpBackendRequest req = invocation.getArgument(0, HttpBackendRequest.class);

                    // assert
                    assertTrue(req.getHeaders().containsKey(Constants.HEADER_IOV42_CLAIMS));
                    assertTrue(req.getHeaders().get(Constants.HEADER_IOV42_CLAIMS).contains(Constants.ENDORSER_ONLY_CLAIM_HEADER_VALUE));

                    return CompletableFuture.completedFuture(responseOk(""));
                });

        // act
        CreateIdentityEndorsementsRequest request = new CreateIdentityEndorsementsRequest(identityId, actor.getIdentityId(), endorsements);
        AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request).authorise(actor);
        platformClient.send(authorisedRequest, actor);
    }

    @Test
    void noNodeIdShouldCauseRetrievingNodeId() {

        // prepare
        String requestId = "req-1";
        String identityId = "5678";

        String nodeIdRequestUrl = PLATFORM_HOST_URL + "/api/v1/node-info";
        when(httpBackend.execute(TestHelper.get(nodeIdRequestUrl)))
                .thenReturn(responseOk(new NodeInfoResponse("1234", new PublicCredentials(ProtocolType.SHA256_WITH_RSA, "key"))));

        String testRequestUrl = PLATFORM_HOST_URL + "/api/v1/identities/" + identityId + "/public-key?requestId=req-1&nodeId=1234";
        when(httpBackend.execute(TestHelper.get(testRequestUrl)))
                .thenReturn(responseOk(new PublicCredentials(ProtocolType.SHA256_WITH_RSA, "mock-key")));

        SignatoryInfo actor = randomSignatoryInfo();

        // act
        GetIdentityRequest request = new GetIdentityRequest(requestId, identityId);
        PublicCredentials response = platformClient.queryAs(actor).getIdentityPublicKey(request);

        // assert
        assertEquals("mock-key", response.getKey());
        verify(httpBackend, times(1)).execute(TestHelper.get(nodeIdRequestUrl));
        verify(httpBackend, times(1)).execute(TestHelper.get(testRequestUrl));
    }

    @Test
    void invalidNodeIdShouldCauseRetry() {

        // prepare
        noNodeIdShouldCauseRetrievingNodeId();

        reset(httpBackend);

        String requestId = "req-2";
        String identityId = "5678";

        // 1. that request fails (simulate invalid nodeId)
        String testRequestUrlFail = PLATFORM_HOST_URL + "/api/v1/identities/" + identityId + "/public-key?requestId=req-2&nodeId=1234";
        when(httpBackend.execute(TestHelper.get(testRequestUrlFail)))
                .thenReturn(response(400, "{\"errors\":[{\"errorCode\":1901,\"errorType\":\"System\",\"message\":\"Given node identity does not match the current node's identity\"}],\"requestId\":\"req-2\"}"));

        // 2. that request fetches a new nodeId
        String nodeIdRequestUrl = PLATFORM_HOST_URL + "/api/v1/node-info";
        when(httpBackend.execute(TestHelper.get(nodeIdRequestUrl)))
                .thenReturn(responseOk(new NodeInfoResponse("0815", new PublicCredentials(ProtocolType.SHA256_WITH_RSA, "key"))));

        // 3. that request is a retry of the original request #1 with a new node id that succeeds
        String testRequestUrlSuccess = PLATFORM_HOST_URL + "/api/v1/identities/" + identityId + "/public-key?requestId=req-2&nodeId=0815";
        when(httpBackend.execute(TestHelper.get(testRequestUrlSuccess)))
                .thenReturn(responseOk(new PublicCredentials(ProtocolType.SHA256_WITH_RSA, "mock-key")));

        SignatoryInfo actor = randomSignatoryInfo();

        // act
        GetIdentityRequest request = new GetIdentityRequest(requestId, identityId);
        PublicCredentials response = platformClient.queryAs(actor).getIdentityPublicKey(request);

        // assert
        assertEquals("mock-key", response.getKey());
        verify(httpBackend, times(1)).execute(TestHelper.get(nodeIdRequestUrl));
        verify(httpBackend, times(1)).execute(TestHelper.get(testRequestUrlFail));
        verify(httpBackend, times(1)).execute(TestHelper.get(testRequestUrlSuccess));
    }

    @Test
    void shouldGetNodeInfo() {
        // prepare
        when(httpBackend.execute(any()))
                .thenReturn(responseOk(new NodeInfoResponse("123", new PublicCredentials(ProtocolType.SHA256_WITH_RSA, "key"))));

        // act
        Optional<NodeInfoResponse> res = platformClient.getNodeInfo();

        // assert
        assertTrue(res.isPresent());
        assertEquals("123", res.get().getNodeId());
    }

    @Test
    void shouldGetHealthChecks() {
        // prepare
        when(httpBackend.execute(any()))
                .thenReturn(responseOk(new HealthChecks()));

        // act
        Optional<HealthChecks> res = platformClient.getHealthChecks();

        // assert
        assertTrue(res.isPresent());
    }

    @Test
    void clientErrorShouldThrowException() {
        // prepare
        when(httpBackend.execute(any()))
                .thenReturn(response(400, "{\"errors\":[{\"errorCode\":1234,\"errorType\":\"System\",\"message\":\"Some error with the request\"}],\"requestId\":\"req-1\"}"));

        // act
        PlatformErrorException ex = assertThrows(PlatformErrorException.class, () -> {
            platformClient.getHealthChecks();
        });

        // assert
        assertNotNull(ex);
        assertEquals(1, ex.getErrorResponse().getErrors().size());
        assertEquals("req-1", ex.getErrorResponse().getRequestId());
        assertEquals(1234, ex.getErrorResponse().getErrors().get(0).getErrorCode());
    }

    private static String randomId() {
        return UUID.randomUUID().toString();
    }

    private static SignatoryInfo randomSignatoryInfo() {
        SignatoryInfo y = new SignatoryInfo("id", ProtocolType.SHA256_WITH_ECDSA, PlatformUtils.generateKeyPair(ProtocolType.SHA256_WITH_ECDSA));
        return new SignatoryInfo("id", ProtocolType.SHA256_WITH_RSA, PlatformUtils.generateKeyPair(ProtocolType.SHA256_WITH_RSA));
    }
}
