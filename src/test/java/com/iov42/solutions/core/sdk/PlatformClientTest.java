package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.requests.post.CreateAssetRequest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class PlatformClientTest {

    private final PlatformClient platformClient = mock(PlatformClient.class);

    // TODO Implement here UT's
    @Test
    void name() {
        platformClient.createAsset(mock(CreateAssetRequest.class), mock(IovKeyPair.class));
    }
}
