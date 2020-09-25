package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.DefaultHttpClientProvider;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.get.GetAssetTypeRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityClaimRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityRequest;
import com.iov42.solutions.core.sdk.model.requests.post.CreateAssetTypeRequest;
import com.iov42.solutions.core.sdk.model.requests.post.CreateClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.post.CreateEndorsementsRequest;
import com.iov42.solutions.core.sdk.model.requests.post.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.*;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
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
        context.setKeyPair(new IovKeyPair(context.getIdentityId(), ProtocolType.SHA256WithRSA, SecurityUtils.generateKeyPair()));
        context.setAssetTypeId(UUID.randomUUID().toString());
        context.setAssetTypeQuantifiableId(UUID.randomUUID().toString());
        client = new PlatformClient(URL, new DefaultHttpClientProvider());
    }

    private NodeInfoResponse getNodeInfo() throws Exception {
        Optional<NodeInfoResponse> optInfo = client.getNodeInfo();
        assertTrue(optInfo.isPresent());
        return optInfo.get();
    }

    /**
     * Run Identity Tests together as a group
     */
    @Nested
    @DisplayName("Identity Tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IdentityTests {

        static final String ASSUME_MESSAGE = "In order to perform this test, identity must be created. Run IdentityTest group together.";

        @Test
        @DisplayName("Create Identity Test")
        @Order(1)
        void testCreateIdentity() {
            String requestId = UUID.randomUUID().toString();
            String identityId = context.getIdentityId();
            IovKeyPair keyPair = context.getKeyPair();
            String publicKey = SecurityUtils.encodeBase64(keyPair.getPublicKey());
            PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), publicKey);

            CreateIdentityRequest request = new CreateIdentityRequest(requestId, identityId, credentials);

            client.createIdentity(request, keyPair).whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response);
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
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String requestId = UUID.randomUUID().toString();
            List<String> claims = List.of("claim1", "claim2");
            String subjectId = context.getIdentityId();

            CreateClaimsRequest request = new CreateClaimsRequest(requestId, claims, subjectId);

            client.createIdentityClaims(request, context.getKeyPair())
                    .whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response);
                        assertTrue(info.isPresent());
                        assertNotNull(info.get());
                        assertNotNull(info.get().getProof());
                        assertNotNull(info.get().getResources());
                        assertEquals(requestId, info.get().getRequestId());
                    }).join();
        }

        @Test
        @DisplayName("Endorse Identity Claims Test")
        @Order(6)
        void testEndorseIdentityClaims() {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            // create new identity that represents endorser
            String endorserId = UUID.randomUUID().toString();
            IovKeyPair keyPair = new IovKeyPair(endorserId, ProtocolType.SHA256WithRSA, SecurityUtils.generateKeyPair());
            PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), SecurityUtils.encodeBase64(keyPair.getPublicKey()));
            String requestId = UUID.randomUUID().toString();

            client.createIdentity(new CreateIdentityRequest(requestId, endorserId, credentials), keyPair).whenComplete((response, throwable) -> {
                Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response);
                assertTrue(info.isPresent());
                assertNotNull(info.get());
                assertNotNull(info.get().getProof());
                assertNotNull(info.get().getResources());
                assertEquals(requestId, info.get().getRequestId());
            }).join();

            // endorse identity claims
            String newRequestId = UUID.randomUUID().toString();
            String subjectId = context.getIdentityId();

            SignatoryIOV signatory = new SignatoryIOV(endorserId, keyPair.getProtocolId().name(), keyPair.getPrivateKey());
            List<String> claims = List.of("claim1", "claim2");

            Map<String, String> endorsements = PlatformUtils.endorse(signatory, context.getSubjectId(), claims);

            CreateEndorsementsRequest request = new CreateEndorsementsRequest(newRequestId, subjectId, endorserId, endorsements);

            client.endorseIdentityClaims(request, keyPair)
                    .whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(newRequestId, response);
                        assertTrue(info.isPresent());
                        assertNotNull(info.get());
                        assertNotNull(info.get().getProof());
                        assertNotNull(info.get().getResources());
                        assertEquals(newRequestId, info.get().getRequestId());
                    }).join();
        }

        @Test
        @DisplayName("Get Identity Test")
        @Order(2)
        void testGetIdentity() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String identityId = context.getIdentityId();
            assertNotNull(identityId);

            String nodeId = getNodeInfo().getNodeId();
            String requestId = UUID.randomUUID().toString();

            GetIdentityRequest getIdentityRequest = new GetIdentityRequest(requestId, nodeId, identityId);
            IovKeyPair keyPair = context.getKeyPair();

            GetIdentityResponse identityResponse = client.getIdentity(getIdentityRequest, keyPair);
            assertNotNull(identityResponse);
            assertNotNull(identityResponse.getIdentityId());
            assertEquals(identityId, identityResponse.getIdentityId());
            assertNotNull(identityResponse.getPublicCredentials());
        }

        @Test
        @DisplayName("Get Identity Claim Test (Single Claim)")
        @Order(5)
        void testGetIdentityClaim() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String requestId = UUID.randomUUID().toString();
            String nodeId = getNodeInfo().getNodeId();
            String identityId = context.getIdentityId();
            String hashedClaim = PlatformUtils.getEncodedClaimHash("claim1");

            GetIdentityClaimRequest request = new GetIdentityClaimRequest(requestId, nodeId, identityId, hashedClaim);
            IovKeyPair keyPair = context.getKeyPair();

            ClaimEndorsementsResponse response = client.getIdentityClaim(request, keyPair);
            assertNotNull(response);
            assertNotNull(response.getClaim());
        }

        @Test
        @DisplayName("Get Identity Claims Test")
        @Order(4)
        void testGetIdentityClaims() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String requestId = UUID.randomUUID().toString();
            String nodeId = getNodeInfo().getNodeId();
            String identityId = context.getIdentityId();

            GetIdentityClaimsRequest request = new GetIdentityClaimsRequest(requestId, nodeId, identityId);
            request.setLimit(10);
            IovKeyPair keyPair = context.getKeyPair();

            GetClaimsResponse claimsResponse = client.getIdentityClaims(request, keyPair);
            assertNotNull(claimsResponse);
            assertNotNull(claimsResponse.getClaims());
        }

        @Test
        @DisplayName("Get Identity PublicKey Test")
        @Order(7)
        void testGetIdentityPublicKey() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String identityId = context.getIdentityId();
            IovKeyPair keyPair = context.getKeyPair();

            String nodeId = getNodeInfo().getNodeId();
            String requestId = UUID.randomUUID().toString();

            GetIdentityRequest request = new GetIdentityRequest(requestId, nodeId, identityId);
            PublicCredentials response = client.getIdentityPublicKey(request, keyPair);
            assertNotNull(response.getProtocolId());
            assertNotNull(response.getKey());
        }

        @Test
        @DisplayName("Create Unique Asset Type")
        @Order(8)
        void testCreateUniqueAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            IovKeyPair keyPair = context.getKeyPair();
            String requestId = UUID.randomUUID().toString();
            String assetTypeId = context.getAssetTypeId();

            CreateAssetTypeRequest request = new CreateAssetTypeRequest(requestId, assetTypeId, AssetTypeProperty.UNIQUE);

            client.createAssetType(request, keyPair)
                    .whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response);

                        System.out.println(info);
                        assertTrue(info.isPresent());
                        assertNotNull(info.get());
                        assertNotNull(info.get().getProof());
                        assertNotNull(info.get().getResources());
                        assertEquals(requestId, info.get().getRequestId());
                    }).join();
        }

        @Test
        @DisplayName("Create Quantifiable Asset Type")
        @Order(8)
        void testCreateQuantifiableAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            IovKeyPair keyPair = context.getKeyPair();
            String requestId = UUID.randomUUID().toString();
            String assetTypeId = context.getAssetTypeQuantifiableId();

            CreateAssetTypeRequest request = new CreateAssetTypeRequest(requestId, assetTypeId, AssetTypeProperty.QUANTIFIABLE, 1);

            client.createAssetType(request, keyPair)
                    .whenComplete((response, throwable) -> {
                        Optional<RequestInfoResponse> info = client.handleRedirect(requestId, response);

                        System.out.println(info);
                        assertTrue(info.isPresent());
                        assertNotNull(info.get());
                        assertNotNull(info.get().getProof());
                        assertNotNull(info.get().getResources());
                        assertEquals(requestId, info.get().getRequestId());
                    }).join();
        }

        @Test
        @DisplayName("Get Unique Asset Type")
        @Order(9)
        void testGetUniqueAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String assetTypeId = context.getAssetTypeId();

            String nodeId = getNodeInfo().getNodeId();
            String requestId = UUID.randomUUID().toString();

            GetAssetTypeRequest request = new GetAssetTypeRequest(requestId, nodeId, assetTypeId);
            IovKeyPair keyPair = context.getKeyPair();

            GetAssetTypeResponse assetTypeResponse = client.getAssetType(request, keyPair);
            assertNotNull(assetTypeResponse);
            assertNotNull(assetTypeResponse.getType());
            assertNotNull(assetTypeResponse.getAssetTypeId());
            assertNotNull(assetTypeResponse.getOwnerId());
            assertNotNull(assetTypeResponse.getProof());
        }

        @Test
        @DisplayName("Get Quantifable Asset Type")
        @Order(9)
        void testGetQuantifiableAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            String assetTypeId = context.getAssetTypeQuantifiableId();

            String nodeId = getNodeInfo().getNodeId();
            String requestId = UUID.randomUUID().toString();

            GetAssetTypeRequest request = new GetAssetTypeRequest(requestId, nodeId, assetTypeId);
            IovKeyPair keyPair = context.getKeyPair();

            GetAssetTypeResponse assetTypeResponse = client.getAssetType(request, keyPair);
            assertNotNull(assetTypeResponse);
            assertNotNull(assetTypeResponse.getType());
            assertNotNull(assetTypeResponse.getAssetTypeId());
            assertNotNull(assetTypeResponse.getOwnerId());
            assertNotNull(assetTypeResponse.getProof());
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
