package com.iov42.solutions.core.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iov42.solutions.core.sdk.http.HttpBackend;
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

public class PlatformClientTest {

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

    private static HttpBackendResponse response(int statusCode, Object body) {
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
        return new HttpBackendResponse(null,null, statusCode, bodyStr);
    }

    private static HttpBackendResponse responseOk(Object body) {
        return response(200, body);
    }

    private static CompletableFuture<HttpBackendResponse> future(HttpBackendResponse response) {
        return CompletableFuture.completedFuture(response);
    }

    private static CompletableFuture<HttpBackendResponse> commandResponse(String requestId, String... resources) {
        return future(responseOk(buildRequestInfoResponse(requestId, resources)));
    }

    private static void assertSingleResource(RequestInfoResponse response, String expectedResource) {
        assertEquals(1, response.getResources().size());
        assertEquals(expectedResource, response.getResources().get(0));
    }

    private void whenExecuteCommand(String requestId, String resource) {
        when(httpBackend.executePut(eq(PLATFORM_HOST_URL + "/api/v1/requests/" + requestId), any(), any()))
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
        when(httpBackend.executePut(any(), any(), any()))
                .then(invocation -> {
                    headers.addAll(invocation.getArgument(2));
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
        when(httpBackend.executePut(any(), any(), any()))
                .then(invocation -> {
                    headers.addAll(invocation.getArgument(2));
                    return CompletableFuture.completedFuture(responseOk(""));
                });

        // act
        CreateIdentityEndorsementsRequest request = new CreateIdentityEndorsementsRequest(identityId, actor.getIdentityId(), endorsements);
        AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request).authorise(actor);
        platformClient.send(authorisedRequest, actor);

        // assert
        int claimsHeaderIdx = headers.indexOf(Constants.HEADER_IOV42_CLAIMS);
        assertTrue(claimsHeaderIdx > -1);
        assertEquals(Constants.ENDORSER_ONLY_CLAIM_HEADER_VALUE, headers.get(claimsHeaderIdx + 1));
    }

    @Test
    void noNodeIdShouldCauseRetrievingNodeId() {

        // prepare
        String requestId = "req-1";
        String identityId = "5678";

        String nodeIdRequestUrl = PLATFORM_HOST_URL + "/api/v1/node-info";
        when(httpBackend.executeGet(eq(nodeIdRequestUrl), any()))
                .thenReturn(responseOk(new NodeInfoResponse("1234", new PublicCredentials(ProtocolType.SHA256WithRSA, "key"))));

        String testRequestUrl = PLATFORM_HOST_URL + "/api/v1/identities/" + identityId + "/public-key?requestId=req-1&nodeId=1234";
        when(httpBackend.executeGet(eq(testRequestUrl), any()))
                .thenReturn(responseOk(new PublicCredentials(ProtocolType.SHA256WithRSA, "mock-key")));

        SignatoryInfo actor = randomSignatoryInfo();

        // act
        GetIdentityRequest request = new GetIdentityRequest(requestId, identityId);
        PublicCredentials response = platformClient.queryAs(actor).getIdentityPublicKey(request);

        // assert
        assertEquals("mock-key", response.getKey());
        verify(httpBackend, times(1)).executeGet(eq(nodeIdRequestUrl), any());
        verify(httpBackend, times(1)).executeGet(eq(testRequestUrl), any());
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
        when(httpBackend.executeGet(eq(testRequestUrlFail), any()))
                .thenReturn(response(400, "{\"errors\":[{\"errorCode\":1901,\"errorType\":\"System\",\"message\":\"Given node identity does not match the current node's identity\"}],\"requestId\":\"req-2\"}"));

        // 2. that request fetches a new nodeId
        String nodeIdRequestUrl = PLATFORM_HOST_URL + "/api/v1/node-info";
        when(httpBackend.executeGet(eq(nodeIdRequestUrl), any()))
                .thenReturn(responseOk(new NodeInfoResponse("0815", new PublicCredentials(ProtocolType.SHA256WithRSA, "key"))));

        // 3. that request is a retry of the original request #1 with a new node id that succeeds
        String testRequestUrlSuccess = PLATFORM_HOST_URL + "/api/v1/identities/" + identityId + "/public-key?requestId=req-2&nodeId=0815";
        when(httpBackend.executeGet(eq(testRequestUrlSuccess), any()))
                .thenReturn(responseOk(new PublicCredentials(ProtocolType.SHA256WithRSA, "mock-key")));

        SignatoryInfo actor = randomSignatoryInfo();

        // act
        GetIdentityRequest request = new GetIdentityRequest(requestId, identityId);
        PublicCredentials response = platformClient.queryAs(actor).getIdentityPublicKey(request);

        // assert
        assertEquals("mock-key", response.getKey());
        verify(httpBackend, times(1)).executeGet(eq(nodeIdRequestUrl), any());
        verify(httpBackend, times(1)).executeGet(eq(testRequestUrlFail), any());
        verify(httpBackend, times(1)).executeGet(eq(testRequestUrlSuccess), any());
    }

    @Test
    void shouldGetNodeInfo() {
        // prepare
        when(httpBackend.executeGet(any(), any()))
                .thenReturn(responseOk(new NodeInfoResponse("123", new PublicCredentials(ProtocolType.SHA256WithRSA, "key"))));

        // act
        Optional<NodeInfoResponse> res = platformClient.getNodeInfo();

        // assert
        assertTrue(res.isPresent());
        assertEquals("123", res.get().getNodeId());
    }

    @Test
    void shouldGetHealthChecks() {
        // prepare
        when(httpBackend.executeGet(any(), any()))
                .thenReturn(responseOk(new HealthChecks()));

        // act
        Optional<HealthChecks> res = platformClient.getHealthChecks();

        // assert
        assertTrue(res.isPresent());
    }

    @Test
    void clientErrorShouldThrowException() {
        // prepare
        when(httpBackend.executeGet(any(), any()))
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
        SignatoryInfo y = new SignatoryInfo("id", ProtocolType.SHA256WithECDSA, PlatformUtils.generateKeyPair(ProtocolType.SHA256WithECDSA));
        return new SignatoryInfo("id", ProtocolType.SHA256WithRSA, PlatformUtils.generateKeyPair(ProtocolType.SHA256WithRSA));
    }
}
