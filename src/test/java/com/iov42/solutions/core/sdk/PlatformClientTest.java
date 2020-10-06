package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.errors.PlatformException;
import com.iov42.solutions.core.sdk.http.DefaultHttpClientProvider;
import com.iov42.solutions.core.sdk.http.HttpClientProvider;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.requests.post.*;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.JsonUtils;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlatformClientTest {

    private static final HttpClient httpClient = spy(HttpClient.class);
    private static final HttpResponse httpResponse = spy(HttpResponse.class);
    private static final String EMPTY_RESPONSE = "{}";
    private static PlatformClient platformClient;

    @BeforeAll
    public static void before() throws IOException, InterruptedException {
        HttpClientProvider provider = new DefaultHttpClientProvider(httpClient);
        platformClient = new PlatformClient("http://", provider);
    }

    @Test
    void shouldGetNodeInfo() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildNodeInfoResponse()));
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        Optional<NodeInfoResponse> res = platformClient.getNodeInfo();

        // then
        assertTrue(res.isPresent());
    }

    @Test
    void shouldGetHealthChecks() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(EMPTY_RESPONSE);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);
        // when
        Optional<HealthChecks> res = platformClient.getHealthChecks();

        // then
        assertTrue(res.isPresent());
    }

    @Test
    void shouldCreateIdentity() throws IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        CreateIdentityRequest request = new CreateIdentityRequest("", "", buildNodeInfoResponse().getPublicCredentials());
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.createIdentity(request, iovKeyPair())
                .whenComplete((r, throwable) -> {

                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldCreateAsset() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        CreateAssetUniqueRequest request = new CreateAssetUniqueRequest("", "", "");
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.createAsset(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldCreateAssetType() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        CreateAssetTypeRequest request = new CreateAssetTypeRequest("", "", AssetTypeProperty.QUANTIFIABLE);
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.createAssetType(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetAssetType() throws PlatformException, IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildGetAssetTypeRespone()));
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        GetAssetTypeResponse res = platformClient.getAssetType(mock(GetAssetTypeRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetAsset() throws PlatformException, IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(EMPTY_RESPONSE);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        GetAssetResponse res = platformClient.getAsset(mock(GetAssetRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldCreateIdentityClaims() throws IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        CreateClaimsRequest request = new CreateClaimsRequest(List.of(), "");
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.createIdentityClaims(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetIdentity() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildGetIdentityResponse()));
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        GetIdentityResponse res = platformClient.getIdentity(mock(GetIdentityRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldTransfer() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        TransferRequest request = new TransferRequest("", List.of());
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.transfer(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldEndorseIdentityClaims() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        CreateEndorsementsRequest request = new CreateEndorsementsRequest("", "", Map.of());
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.endorseIdentityClaims(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetIdentityClaim() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(EMPTY_RESPONSE);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        ClaimEndorsementsResponse res = platformClient.getIdentityClaim(mock(GetIdentityClaimRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetIdentityClaims() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(EMPTY_RESPONSE);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        GetClaimsResponse res = platformClient.getIdentityClaims(mock(GetIdentityClaimsRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetIdentityPublicKey() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(EMPTY_RESPONSE);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        PublicCredentials res = platformClient.getIdentityPublicKey(mock(GetIdentityRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetRequest() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildRequestInfoResponse()));
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        RequestInfoResponse res = platformClient.getRequest("id");

        // then
        assertNotNull(res);
    }

    private NodeInfoResponse buildNodeInfoResponse() {
        return new NodeInfoResponse("node1", new PublicCredentials("pid", "key"));
    }

    private GetAssetTypeResponse buildGetAssetTypeRespone() {
        return new GetAssetTypeResponse("proof", "atid", "ownerid", "type", 0);
    }

    private RequestInfoResponse buildRequestInfoResponse() {
        return new RequestInfoResponse("proof", "id", new String[0]);
    }

    private HealthChecks buildHealthChecksResponse() {
        return new HealthChecks();
    }

    private IovKeyPair iovKeyPair() {
        return new IovKeyPair("id", ProtocolType.SHA256WithRSA, SecurityUtils.generateKeyPair());
    }

    private GetIdentityResponse buildGetIdentityResponse() {
        return new GetIdentityResponse("proof", "id", List.of());
    }

}
