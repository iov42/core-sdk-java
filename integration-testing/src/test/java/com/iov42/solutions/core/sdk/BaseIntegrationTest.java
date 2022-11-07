package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.command.*;
import com.iov42.solutions.core.sdk.model.responses.BaseResponse;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BaseIntegrationTest.ExceptionWatcher.class)
public abstract class BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(BaseIntegrationTest.class);

    private PlatformClient platformClient;

    protected PlatformClient client() {
        return platformClient;
    }

    static class ExceptionWatcher implements TestWatcher {

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            while (cause instanceof Exception) {
                Exception ex = (Exception) cause;
                if (ex.toString().contains("Insert Platform URL here")) {
                    log.error("Please contact iov42 (at https://iov42.com/contact/) to request a valid platform URL!");
                    break;
                }
                cause = ex.getCause();
            }
        }
    }

    @BeforeEach
    public void init() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream("integration-test.properties")) {
            Properties props = new Properties();
            props.load(input);
            String url = props.getProperty("platform-url");
            platformClient = new PlatformClient(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait some amount of time to let the platform propagate state changes for subsequent read operations.
     * This is required in this scenario as we create entities and immediately read them afterwards.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Eventual_consistency">Eventual consistency</a>
     *
     */
    protected void ensureEventualConsistency() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected String randomId() {
        return UUID.randomUUID().toString();
    }

    protected SignatoryInfo createIdentity(String identityId) {
        String requestId = UUID.randomUUID().toString();
        SignatoryInfo signatoryInfo = generateSignatoryInfo(identityId, ProtocolType.SHA256_WITH_RSA);
        String publicKey = PlatformUtils.encodeBase64(signatoryInfo.getPublicKey());
        PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256_WITH_RSA, publicKey);

        CreateIdentityRequest request = new CreateIdentityRequest(requestId, identityId, credentials);
        platformClient.send(request, signatoryInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        return signatoryInfo;
    }

    protected void createUniqueAssetType(SignatoryInfo ownerInfo, String assetTypeId) {
        var createAssetTypeRequest = CreateAssetTypeRequest.unique(assetTypeId);

        client().send(createAssetTypeRequest, ownerInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();
    }

    protected void createQuantifiableAssetType(SignatoryInfo ownerInfo, String assetTypeId, int scale) {
        var createAssetTypeRequest = CreateAssetTypeRequest.quantifiable(assetTypeId, scale);
        client().send(createAssetTypeRequest, ownerInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();
    }

    protected void createAssetTypeClaims(SignatoryInfo signatoryInfo, String assetTypeId, Claims claims) {
        var createAssetTypeClaimsRequest = new CreateAssetTypeClaimsRequest(assetTypeId, claims);
        client().send(createAssetTypeClaimsRequest, signatoryInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();
    }

    protected void createUniqueAsset(SignatoryInfo ownerInfo, String assetTypeId, String assetId) {
        var createAssetRequest = new CreateAssetRequest(assetTypeId, assetId);
        client().send(createAssetRequest, ownerInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();
    }

    protected void createQuantifiableAsset(SignatoryInfo ownerInfo, String assetTypeId, String assetId, int quantity) {
        var createAssetRequest = new CreateAssetRequest(assetTypeId, assetId, BigInteger.valueOf(quantity));
        client().send(createAssetRequest, ownerInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();
    }

    protected void transferQuantity(String assetTypeId, String senderAccountId, SignatoryInfo sender, String receiverAccountId) {
        var transferRequest =
                TransfersRequest.of(new TransferQuantity(assetTypeId, senderAccountId, receiverAccountId, 50));
        client().send(transferRequest, sender)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();
    }

    protected void createAssetClaims(SignatoryInfo signatoryInfo, String assetTypeId, String assetId, Claims claims) {
        var createAssetClaimsRequest = new CreateAssetClaimsRequest(assetTypeId, assetId, claims);
        client().send(createAssetClaimsRequest, signatoryInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();
    }

    protected void transferOwnership(String assetTypeId, String assetId, SignatoryInfo senderInfo, String receiverId) {
        var transferRequest =
                TransfersRequest.of(new TransferOwnership(assetTypeId, assetId, senderInfo.getIdentityId(), receiverId));
        client().send(transferRequest, senderInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();
    }

    protected static SignatoryInfo generateSignatoryInfo(String identityId, ProtocolType protocolType) {
        return new SignatoryInfo(identityId, protocolType, PlatformUtils.generateKeyPair(protocolType));
    }

    protected static void assertResources(Collection<String> resources, String...assertions) {

        var set = Arrays.stream(assertions).collect(Collectors.toSet());
        for (var resource : resources) {
            for (var assertion : set) {
                if (resource.endsWith(assertion)) {
                    set.remove(assertion);
                    break;
                }
            }
        }
        if (set.size() > 0) {
            var sb = new StringBuilder();
            sb.append("Resource assertions failed. Could not find ");
            for (var failure : set) {
                sb.append(failure).append(" ");
            }
            fail(sb.toString());
        }
    }

    protected static void assertResponseSuccess(BaseResponse response, Throwable throwable) {
        assertNotNull(response);
        if (throwable instanceof PlatformErrorException) {
            var errorResponse = ((PlatformErrorException) throwable).getErrorResponse();
            assertNotNull(errorResponse);
            if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
                var sb = new StringBuilder();
                sb.append("Request failed: ");
                for (var error : errorResponse.getErrors()) {
                    sb.append(error).append(" ");
                }
                fail(sb.toString());
            }
        }
        if (response instanceof RequestInfoResponse) {
            assertTrue(((RequestInfoResponse) response).hasFinished());
        }
    }
}
