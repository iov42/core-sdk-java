package com.iov42.solutions.core.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatformTest extends BaseIntegrationTest {

    @Test
    void healthChecksShouldBeValid() {
        var checks = client().getHealthChecks();
        assertTrue(checks.isPresent());
        assertNotNull(checks.get().getBuildInfo().getName());
        assertNotNull(checks.get().getBuildInfo().getVersion());
        assertNotNull(checks.get().getBuildInfo().getScalaVersion());
        assertNotNull(checks.get().getBuildInfo().getSbtVersion());

        assertTrue(checks.get().getBroker().getCanWrite());

        assertTrue(checks.get().getRequestStore().getCanRead());
        assertTrue(checks.get().getRequestStore().getCanWrite());

        assertTrue(checks.get().getAssetStore().getCanRead());
        assertTrue(checks.get().getAssetStore().getCanWrite());

        assertTrue(checks.get().getClaimStore().getCanRead());
        assertTrue(checks.get().getClaimStore().getCanWrite());

        assertTrue(checks.get().getEndorsementStore().getCanRead());
        assertTrue(checks.get().getEndorsementStore().getCanWrite());

        assertTrue(checks.get().getPermissionStore().getCanRead());
        assertTrue(checks.get().getPermissionStore().getCanWrite());

        assertTrue(checks.get().getProofStore().getCanRead());
        assertTrue(checks.get().getProofStore().getCanWrite());

        assertTrue(checks.get().getTransactionStore().getCanRead());
        assertTrue(checks.get().getTransactionStore().getCanWrite());

        assertTrue(checks.get().getTransactionEntryForReaderStore().getCanRead());
        assertTrue(checks.get().getTransactionEntryForReaderStore().getCanWrite());

        assertTrue(checks.get().getHsm().getHasKeys());
    }
}
