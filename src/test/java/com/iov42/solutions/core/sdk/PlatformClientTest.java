package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.DefaultHttpClientProvider;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.CreateClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.requests.GetIdentityClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.GetIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.GetClaimsResponse;
import com.iov42.solutions.core.sdk.model.responses.GetIdentityResponse;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PlatformClientTest {

    private static final String URL = "https://api.sandbox.iov42.dev/api";

    private static PlatformClient client;

    private static TestContext context;

    @BeforeAll
    public static void init() {
        context = new TestContext();
        context.setIdentityId(UUID.randomUUID().toString());
        context.setRequestId(UUID.randomUUID().toString());
        context.setKeyPair(new IovKeyPair(context.getIdentityId(), ProtocolType.SHA256WithRSA, SecurityUtils.generateKeyPair()));

        client = new PlatformClient(URL, new DefaultHttpClientProvider());
    }

    private NodeInfoResponse getNodeInfo() throws Exception {
        Optional<NodeInfoResponse> optInfo = client.getNodeInfo();
        assertTrue(optInfo.isPresent());
        return optInfo.get();
    }

    @Nested
    @DisplayName("Identity Tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IdentityTests {

        @Test
        @DisplayName("Create Identity Test")
        @Order(1)
        void testCreateIdentity() {
            String requestId = context.getRequestId();
            String identityId = context.getIdentityId();
            IovKeyPair keyPair = context.getKeyPair();
            String publicKey = SecurityUtils.encodeBase64(keyPair.getPublicKey());
            PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), publicKey);

            CreateIdentityRequest request = new CreateIdentityRequest(requestId, identityId, credentials);

            client.createIdentity(request, keyPair).whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response.headers());
                        assertTrue(info.isPresent());
                        assertNotNull(info.get());
                        assertNotNull(info.get().getProof());
                        assertNotNull(info.get().getResources());
                        assertEquals(requestId, info.get().getRequestId());
                        context.setCreatedIdentity(true);
                    }
            ).join();
        }

        @Test
        @DisplayName("Create Identity Claims Test")
        @Order(3)
        void testCreateIdentityClaims() {
            assumeTrue(context.isCreatedIdentity(), "In order to perform this test, identity must be created. Run IdentityTest together.");

            String requestId = UUID.randomUUID().toString();

            CreateClaimsRequest request = new CreateClaimsRequest();
            request.setRequestId(requestId);
            request.setClaims(List.of("claim1", "claim2"));
            request.setSubjectId(context.getIdentityId());

            client.createIdentityClaims(request, context.getKeyPair())
                    .whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response.headers());
                        assertTrue(info.isPresent());
                        assertNotNull(info.get());
                        assertNotNull(info.get().getProof());
                        assertNotNull(info.get().getResources());
                        assertEquals(requestId, info.get().getRequestId());
                    }).join();
        }

        @Test
        @DisplayName("Get Identity Test")
        @Order(2)
        void testGetIdentity() throws Exception {
            assumeTrue(context.isCreatedIdentity(), "In order to perform this test, identity must be created. Run IdentityTest together.");

            String identityId = context.getIdentityId();

            IovKeyPair keyPair = context.getKeyPair();
            assertNotNull(identityId);

            String nodeId = getNodeInfo().getNodeId();
            String newRequestId = UUID.randomUUID().toString();

            GetIdentityRequest getIdentityRequest = new GetIdentityRequest(newRequestId, identityId, nodeId);
            GetIdentityResponse identityResponse = client.getIdentity(getIdentityRequest, keyPair);
            assertNotNull(identityResponse);
            assertNotNull(identityResponse.getIdentityId());
            assertEquals(identityId, identityResponse.getIdentityId());
            assertNotNull(identityResponse.getPublicCredentials());
        }

        @Test
        @DisplayName("Get Identity Claims Test")
        @Order(4)
        void testGetIdentityClaims() throws Exception {
            assumeTrue(context.isCreatedIdentity(), "In order to perform this test, identity must be created. Run IdentityTest together.");

            GetIdentityClaimsRequest request = new GetIdentityClaimsRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setIdentityId(context.getIdentityId());
            request.setNodeId(getNodeInfo().getNodeId());
            request.setLimit(10);

            GetClaimsResponse claimsResponse = client.getIdentityClaims(request, context.getKeyPair());
            assertNotNull(claimsResponse);
            assertNotNull(claimsResponse.getClaims());
        }
    }

    @Nested
    @DisplayName("Node Tests")
    class NodeTests {

        @Test
        void testGetHealthChecks() throws Exception {
            Optional<HealthChecks> optInfo = client.getHealthChecks();
            assertTrue(optInfo.isPresent());
            HealthChecks healthChecks = optInfo.get();

            assertNotNull(healthChecks.getBuildInfo());
            assertNotNull(healthChecks.getBuildInfo().getName());
            assertNotNull(healthChecks.getBuildInfo().getVersion());
        }

        @Test
        void testGetNodeInfo() throws Exception {
            NodeInfoResponse info = getNodeInfo();
            assertNotNull(info.getNodeId());
            assertNotNull(info.getPublicCredentials());
            assertNotNull(info.getPublicCredentials().getKey());
            assertNotNull(info.getPublicCredentials().getProtocolId());
        }
    }
}
