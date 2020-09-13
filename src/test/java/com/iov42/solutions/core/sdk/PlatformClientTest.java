package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.KeyPairWrapper;
import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.CreateIdentityRequest;
import com.iov42.solutions.core.sdk.model.responses.NodeInfoResponse;
import com.iov42.solutions.core.sdk.utils.HttpUtils;
import com.iov42.solutions.core.sdk.utils.SecurityUtils;
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

    private final PlatformClient client = new PlatformClient(URL);

    @Test
    public void testCreateIdentity() throws Exception {
        String identityId = "I-" + UUID.randomUUID().toString();
        String requestId = "R-" + UUID.randomUUID().toString();

        KeyPair keyPair = SecurityUtils.generateKeyPair();

        String publicKey = SecurityUtils.encodeBase64(keyPair.getPublic().getEncoded());
        PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA.name(), publicKey);

        KeyPairWrapper keyPairWrapper = new KeyPairWrapper(identityId, ProtocolType.SHA256WithRSA, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());

        CreateIdentityRequest request = new CreateIdentityRequest(requestId, identityId, credentials);

        Consumer<HttpHeaders> redirectConsumer = httpHeaders -> httpHeaders.firstValue("location").ifPresent(l -> {
            try {
                HttpResponse<String> response = HttpUtils.get(PlatformClient.DEFAULT_URL + l);
                assertNotNull(response.body());
                // assertEquals(identityId, response.body());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        client.createIdentity(request, keyPairWrapper)
                .thenApply(HttpResponse::headers)
                .thenAccept(redirectConsumer).join();
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
