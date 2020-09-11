package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.security.KeyPair;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlatformClientTest {

    private static final String URL = "https://api.sandbox.iov42.dev/api";

    private final PlatformClient client = new PlatformClient(URL);

    @Test
    public void testCreateIdentity() throws Exception {
        String identityId = "Identity-" + UUID.randomUUID().toString();
        String requestId = "Request-" + UUID.randomUUID().toString();

        KeyPair keyPair = SecurityUtils.generateKeyPair();

        String publicKey = SecurityUtils.encodeBase64(keyPair.getPublic().getEncoded());
        PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), publicKey);

        KeyPairData keyPairData = new KeyPairData(identityId, ProtocolType.SHA256WithRSA, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());

        CreateIdentityRequest request = new CreateIdentityRequest(identityId, credentials);

        CompletableFuture<HttpResponse<String>> response = client.createIdentity(request, keyPairData);
        assertNotNull(response);
        HttpResponse<String> stringHttpResponse = response.get();
        assertNotNull(stringHttpResponse);
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
        Optional<NodeInfoResponse> optInfo = client.getNodeInfo();
        assertTrue(optInfo.isPresent());

        NodeInfoResponse info = optInfo.get();
        assertNotNull(info.getNodeId());
        assertNotNull(info.getPublicCredentials());
        assertNotNull(info.getPublicCredentials().getKey());
        assertNotNull(info.getPublicCredentials().getProtocolId());
    }
}
