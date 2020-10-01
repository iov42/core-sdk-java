package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.DefaultHttpClientProvider;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.requests.post.*;
import com.iov42.solutions.core.sdk.model.responses.GetAssetTypeResponse;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


public class PlatformClientIntegrationTest {

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
        context.setAssetId(UUID.randomUUID().toString());
        client = new PlatformClient(URL, new DefaultHttpClientProvider());
    }

    private NodeInfoResponse getNodeInfo() throws Exception {
        var optInfo = client.getNodeInfo();
        assertTrue(optInfo.isPresent());
        return optInfo.get();
    }

    private void assertRequestInfoResponse(Optional<RequestInfoResponse> item, String resourceId) {
        assertTrue(item.isPresent() && item.get().getProof() != null && item.get().getResources() != null);
        assertEquals(resourceId, item.get().getRequestId());
    }

    @Nested
    @DisplayName("Node Tests")
    class NodeTests {

        @Test
        void testGetHealthChecks() throws Exception {
            var optInfo = client.getHealthChecks();
            assertTrue(optInfo.isPresent());
            var healthChecks = optInfo.get();

            assertNotNull(healthChecks.getBuildInfo());
            assertNotNull(healthChecks.getBuildInfo().getName());
            assertNotNull(healthChecks.getBuildInfo().getVersion());
        }

        @Test
        void testGetNodeInfo() throws Exception {
            var info = getNodeInfo();
            assertNotNull(info.getNodeId());
            assertNotNull(info.getPublicCredentials());
            assertNotNull(info.getPublicCredentials().getKey());
            assertNotNull(info.getPublicCredentials().getProtocolId());
        }
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
            var requestId = UUID.randomUUID().toString();
            var identityId = context.getIdentityId();
            var keyPair = context.getKeyPair();
            var publicKey = SecurityUtils.encodeBase64(keyPair.getPublicKey());
            var credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), publicKey);

            var request = new CreateIdentityRequest(requestId, identityId, credentials);

            client.createIdentity(request, context.getKeyPair())
                    .whenComplete((response, throwable) -> {
                        assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId);
                        context.setCreatedIdentity(true);
                    }).join();

        }

        @Test
        @DisplayName("Create Identity Claims Test")
        @Order(3)
        void testCreateIdentityClaims() {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var requestId = UUID.randomUUID().toString();
            var claims = List.of("claim1", "claim2");
            var subjectId = context.getIdentityId();

            var request = new CreateClaimsRequest(requestId, claims, subjectId);

            client.createIdentityClaims(request, context.getKeyPair())
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();

        }

        @Test
        @DisplayName("Endorse Identity Claims Test")
        @Order(6)
        void testEndorseIdentityClaims() {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            // create new identity that represents endorser
            var endorserId = UUID.randomUUID().toString();
            var keyPair = new IovKeyPair(endorserId, ProtocolType.SHA256WithRSA, SecurityUtils.generateKeyPair());
            var credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), SecurityUtils.encodeBase64(keyPair.getPublicKey()));
            var requestId = UUID.randomUUID().toString();

            client.createIdentity(new CreateIdentityRequest(requestId, endorserId, credentials), keyPair)
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();

            // endorse identity claims
            var newRequestId = UUID.randomUUID().toString();
            var subjectId = context.getIdentityId();

            var signatory = new SignatoryIOV(endorserId, keyPair.getProtocolId().name(), keyPair.getPrivateKey());
            var claims = List.of("claim1", "claim2");

            var endorsements = PlatformUtils.endorse(signatory, context.getSubjectId(), claims);

            var request = new CreateEndorsementsRequest(newRequestId, subjectId, endorserId, endorsements);

            client.endorseIdentityClaims(request, keyPair)
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();
        }

        @Test
        @DisplayName("Get Identity Test")
        @Order(2)
        void testGetIdentity() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var identityId = context.getIdentityId();
            assertNotNull(identityId);

            var nodeId = getNodeInfo().getNodeId();
            var requestId = UUID.randomUUID().toString();

            var getIdentityRequest = new GetIdentityRequest(requestId, nodeId, identityId);
            var keyPair = context.getKeyPair();

            var identityResponse = client.getIdentity(getIdentityRequest, keyPair);
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

            var requestId = UUID.randomUUID().toString();
            var nodeId = getNodeInfo().getNodeId();
            var identityId = context.getIdentityId();
            var hashedClaim = PlatformUtils.getEncodedClaimHash("claim1");

            var request = new GetIdentityClaimRequest(requestId, nodeId, identityId, hashedClaim);
            var keyPair = context.getKeyPair();

            var response = client.getIdentityClaim(request, keyPair);
            assertNotNull(response);
            assertNotNull(response.getClaim());
        }

        @Test
        @DisplayName("Get Identity Claims Test")
        @Order(4)
        void testGetIdentityClaims() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var requestId = UUID.randomUUID().toString();
            var nodeId = getNodeInfo().getNodeId();
            var identityId = context.getIdentityId();

            var request = new GetIdentityClaimsRequest(requestId, nodeId, identityId);
            request.setLimit(10);
            var keyPair = context.getKeyPair();

            var claimsResponse = client.getIdentityClaims(request, keyPair);
            assertNotNull(claimsResponse);
            assertNotNull(claimsResponse.getClaims());
        }

        @Test
        @DisplayName("Get Identity PublicKey Test")
        @Order(7)
        void testGetIdentityPublicKey() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var identityId = context.getIdentityId();
            var keyPair = context.getKeyPair();

            var nodeId = getNodeInfo().getNodeId();
            var requestId = UUID.randomUUID().toString();

            var request = new GetIdentityRequest(requestId, nodeId, identityId);
            var response = client.getIdentityPublicKey(request, keyPair);
            assertNotNull(response.getProtocolId());
            assertNotNull(response.getKey());
        }

        @Test
        @DisplayName("Create Unique Asset Type")
        @Order(8)
        void testCreateUniqueAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var keyPair = context.getKeyPair();
            var requestId = UUID.randomUUID().toString();
            var assetTypeId = context.getAssetTypeId();

            var request = new CreateAssetTypeRequest(requestId, assetTypeId, AssetTypeProperty.UNIQUE);

            client.createAssetType(request, keyPair)
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();
        }

        @Test
        @DisplayName("Create Quantifiable Asset Type")
        @Order(8)
        void testCreateQuantifiableAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var keyPair = context.getKeyPair();
            var requestId = UUID.randomUUID().toString();
            var assetTypeId = context.getAssetTypeQuantifiableId();

            var request = new CreateAssetTypeRequest(requestId, assetTypeId, AssetTypeProperty.QUANTIFIABLE, 1);

            client.createAssetType(request, keyPair)
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();
        }

        @Test
        @DisplayName("Get Unique Asset Type")
        @Order(9)
        void testGetUniqueAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var assetTypeId = context.getAssetTypeId();

            var nodeId = getNodeInfo().getNodeId();
            var requestId = UUID.randomUUID().toString();

            var request = new GetAssetTypeRequest(requestId, nodeId, assetTypeId);
            var keyPair = context.getKeyPair();

            var assetTypeResponse = client.getAssetType(request, keyPair);

            assertNotNull(assetTypeResponse);
            assertNotNull(assetTypeResponse.getType());
            assertNotNull(assetTypeResponse.getAssetTypeId());
            assertNotNull(assetTypeResponse.getOwnerId());
            assertNotNull(assetTypeResponse.getProof());
        }

        @Test
        @DisplayName("Get Quantifiable Asset Type")
        @Order(9)
        void testGetQuantifiableAssetType() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var assetTypeId = context.getAssetTypeQuantifiableId();

            var nodeId = getNodeInfo().getNodeId();
            var requestId = UUID.randomUUID().toString();

            var request = new GetAssetTypeRequest(requestId, nodeId, assetTypeId);
            var keyPair = context.getKeyPair();

            GetAssetTypeResponse assetTypeResponse = client.getAssetType(request, keyPair);
            assertNotNull(assetTypeResponse);
            assertNotNull(assetTypeResponse.getType());
            assertNotNull(assetTypeResponse.getAssetTypeId());
            assertNotNull(assetTypeResponse.getOwnerId());
            assertNotNull(assetTypeResponse.getProof());
        }

        @Test
        @DisplayName("Create Asset")
        @Order(10)
        void testCreateAsset() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var keyPair = context.getKeyPair();
            var requestId = UUID.randomUUID().toString();
            var assetTypeId = context.getAssetTypeId();
            var assetId = context.getAssetId();

            var request = new CreateAssetRequest(requestId, assetId, assetTypeId);

            client.createAsset(request, keyPair)
                    .whenComplete((response, throwable) -> {
                        assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId);
                        context.setAssetId(context.getAssetId());
                    }).join();
        }

        @Test
        @DisplayName("Get Asset")
        @Order(11)
        void testGetAsset() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var assetTypeId = context.getAssetTypeId();
            var assetId = context.getAssetId();

            var nodeId = getNodeInfo().getNodeId();
            var requestId = UUID.randomUUID().toString();

            var request = new GetAssetRequest(requestId, nodeId, assetTypeId, assetId);
            var keyPair = context.getKeyPair();

            var assetTypeResponse = client.getAsset(request, keyPair);

            assertNotNull(assetTypeResponse);
            //assertNotNull(assetTypeResponse.getAssetId());
            assertNotNull(assetTypeResponse.getAssetTypeId());
            assertNotNull(assetTypeResponse.getOwnerId());
            assertNotNull(assetTypeResponse.getProof());
        }

        @Test
        @DisplayName("Transfer Ownership")
        @Order(12)
        void testTransferAssets() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var requestId = UUID.randomUUID().toString();
            var keyPair = context.getKeyPair();

            var item = new TransferItem(context.getAssetId(), context.getAssetTypeId(), context.getIdentityId(), context.getIdentityId());
            var request = new TransferRequest(requestId, new TransferItem[]{item});

            client.transferQuantity(request, keyPair)
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();
        }

        @Test
        @DisplayName("Transfer Quantity")
        @Order(12)
        void testTransferOwnership() throws Exception {
            assumeTrue(context.isCreatedIdentity(), ASSUME_MESSAGE);

            var requestId = UUID.randomUUID().toString();
            var keyPair = context.getKeyPair();

            var item = new TransferItem(context.getAssetId(), context.getAssetTypeId(), context.getIdentityId(), context.getIdentityId());
            var request = new TransferRequest(requestId, new TransferItem[]{item});

            client.transferQuantity(request, keyPair)
                    .whenComplete((response, throwable) -> assertRequestInfoResponse(client.handleRedirect(requestId, response), requestId)).join();
        }

    }
}
