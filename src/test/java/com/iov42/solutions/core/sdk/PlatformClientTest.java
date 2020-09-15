package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.DefaultHttpClientProvider;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.AsyncRequestInfo;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.security.KeyPair;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class PlatformClientTest {

    private static final String URL = "https://api.sandbox.iov42.dev/api";

    private PlatformClient client;

    @BeforeEach
    public void setup() {
        client = new PlatformClient(URL, new DefaultHttpClientProvider());
    }

    @Test
    public void testCreateIdentity() throws Exception {
        String identityId = "I-" + UUID.randomUUID().toString();
        String requestId = "R-" + UUID.randomUUID().toString();

        KeyPair keyPair = SecurityUtils.generateKeyPair();

        String publicKey = SecurityUtils.encodeBase64(keyPair.getPublic().getEncoded());
        PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), publicKey);

        IovKeyPair iovKeyPair = new IovKeyPair(identityId, ProtocolType.SHA256WithRSA, keyPair);

        CreateIdentityRequest request = new CreateIdentityRequest(requestId, identityId, credentials);

        Consumer<HttpHeaders> redirectHandler = httpHeaders -> {
            Optional<String> optLocation = httpHeaders.firstValue("location");
            Optional<String> optReTryAfter = httpHeaders.firstValue("retry-after");
            optLocation.ifPresent(location -> {
                try {
                    if (optReTryAfter.isPresent()) {
                        Thread.sleep(Long.parseLong(optReTryAfter.get()) * 1000);
                    }

                    /* location if present = /api/v1/requests/{requestId} */
                    AsyncRequestInfo asyncRequestInfo = client.getRequest(requestId);
                    assertNotNull(asyncRequestInfo);
                    assertNotNull(asyncRequestInfo.getProof());
                    assertNotNull(asyncRequestInfo.getResources());
                    assertEquals(requestId, asyncRequestInfo.getRequestId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        };
        client.createIdentity(request, iovKeyPair)
                .thenApply(HttpResponse::headers)
                .thenAccept(redirectHandler).join();

        //TODO: FIX getIdentity
        /*String nodeId = getNodeInfo().getNodeId();
        String newReqId = UUID.randomUUID().toString();

        AsyncRequestInfo identity = client.getIdentity(identityId, newReqId, nodeId, keyPairWrapper);
        assertNotNull(identity);*/
    }

    @Test
    public void testGetHealthChecks() throws Exception {
        Optional<HealthChecks> optInfo = client.getHealthChecks();
        assertTrue(optInfo.isPresent());
        HealthChecks healthChecks = optInfo.get();

        assertNotNull(healthChecks.getBuildInfo());
        assertNotNull(healthChecks.getBuildInfo().getName());
        assertNotNull(healthChecks.getBuildInfo().getVersion());
    }

    @Test
    public void testGetNodeInfo() throws Exception {
        NodeInfoResponse info = getNodeInfo();
        assertNotNull(info.getNodeId());
        assertNotNull(info.getPublicCredentials());
        assertNotNull(info.getPublicCredentials().getKey());
        assertNotNull(info.getPublicCredentials().getProtocolId());
    }

    private Consumer<HttpHeaders> getHttpHeadersConsumer() {
        return headers -> {
        };
    }

    private NodeInfoResponse getNodeInfo() throws Exception {
        Optional<NodeInfoResponse> optInfo = client.getNodeInfo();
        assertTrue(optInfo.isPresent());
        return optInfo.get();
    }
}
