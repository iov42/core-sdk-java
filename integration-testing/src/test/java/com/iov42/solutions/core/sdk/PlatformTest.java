package com.iov42.solutions.core.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatformTest extends BaseIntegrationTest {

    @Test
    void healthChecksShouldBeValid() {
        var checks = client().getHealthChecks();
        assertTrue(checks.isPresent());
        assertTrue(checks.get().isHealthy());
        assertNotNull(checks.get().getBuildInfo().getName());
        assertNotNull(checks.get().getBuildInfo().getVersion());
        assertNotNull(checks.get().getBuildInfo().getScalaVersion());
        assertNotNull(checks.get().getBuildInfo().getSbtVersion());
    }
}
