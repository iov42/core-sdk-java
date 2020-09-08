package com.iov42.solutions.core.sdk.http;

import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.NodeInfo;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlatformClientTest {

    private static final String URL = "https://api.sandbox.iov42.dev/api";

    private PlatformClient client = new PlatformClient(URL);

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
        Optional<NodeInfo> optInfo = client.getNodeInfo();
        assertTrue(optInfo.isPresent());

        NodeInfo info = optInfo.get();
        assertNotNull(info.getNodeId());
        assertNotNull(info.getPublicCredentials());
        assertNotNull(info.getPublicCredentials().getKey());
        assertNotNull(info.getPublicCredentials().getProtocolId());
    }
}
