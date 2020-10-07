package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.DefaultHttpClientProvider;
import com.iov42.solutions.core.sdk.http.HttpClientProvider;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.requests.put.*;
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

    private static final String EMPTY_RESPONSE = "{}";

    private static final HttpClient httpClient = spy(HttpClient.class);

    private static final HttpResponse httpResponse = spy(HttpResponse.class);

    private static PlatformClient platformClient;

    @BeforeAll
    public static void before() throws IOException, InterruptedException {
        HttpClientProvider provider = new DefaultHttpClientProvider(httpClient);
        platformClient = new PlatformClient("http://", provider);
    }

    @Test
    void shouldCreateAsset() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
        CreateAssetRequest request = new CreateAssetRequest("", "", "");
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
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
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
    void shouldCreateIdentity() throws IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
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
    void shouldCreateIdentityClaims() throws IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
        CreateIdentityClaimsRequest request = new CreateIdentityClaimsRequest("", List.of());
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.createIdentityClaims(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldEndorseIdentityClaims() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
        CreateIdentityEndorsementsRequest request = new CreateIdentityEndorsementsRequest("", "", Map.of());
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.endorseIdentityClaims(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetAsset() throws IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(EMPTY_RESPONSE);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        GetAssetResponse res = platformClient.getAsset(mock(GetAssetRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
    }

    @Test
    void shouldGetAssetType() throws IOException, InterruptedException {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildGetAssetTypeRespone()));
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        GetAssetTypeResponse res = platformClient.getAssetType(mock(GetAssetTypeRequest.class), iovKeyPair());

        // then
        assertNotNull(res);
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
    void shouldGetRequest() throws Exception {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        // when
        BaseResponse res = platformClient.getRequest("id");

        // then
        assertNotNull(res);
    }

    @Test
    void shouldTransfer() {
        // given
        when(httpResponse.body()).thenReturn(JsonUtils.toJson(buildBaseResponse()));
        TransferRequest request = new TransferRequest("", List.of());
        doAnswer(invocation -> CompletableFuture.completedFuture(httpResponse)).when(httpClient).sendAsync(any(), any());

        // when
        HttpResponse<String> res = platformClient.createTransfer(request, iovKeyPair())
                .whenComplete((r, throwable) -> {
                }).join();

        // then
        assertNotNull(res);
    }

    private BaseResponse buildBaseResponse() {
        return new RequestInfoResponse("proof", "id", List.of(""));
    }

    private GetAssetTypeResponse buildGetAssetTypeRespone() {
        return new GetAssetTypeResponse("proof", "atid", "ownerid", "type", 0);
    }

    private GetIdentityResponse buildGetIdentityResponse() {
        return new GetIdentityResponse("proof", "id", List.of());
    }

    private HealthChecks buildHealthChecksResponse() {
        return new HealthChecks();
    }

    private NodeInfoResponse buildNodeInfoResponse() {
        return new NodeInfoResponse("node1", new PublicCredentials("pid", "key"));
    }

    private IovKeyPair iovKeyPair() {
        return new IovKeyPair("id", ProtocolType.SHA256WithRSA, SecurityUtils.generateKeyPair());
    }

}
