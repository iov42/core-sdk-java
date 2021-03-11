package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpHostWrapperTest {

    private static List<String> list(String value) {
        List<String> l = new ArrayList<>();
        l.add(value);
        return l;
    }

    @Test
    public void redirectShouldBeHandled() {
        HttpBackend mockBackend = mock(HttpBackend.class);
        Map<String, List<String>> headers = new HashMap<>();

        headers.put("Location", list("/other/stuff"));
        headers.put("Retry-After", list("1"));

        HttpBackendResponse response = new HttpBackendResponse("", headers, 303, null);
        when(mockBackend.executePut(eq("http://platform.iov42/api/v1/requests/abc"), any(), any())).thenReturn(CompletableFuture.completedFuture(response));
        when(mockBackend.executeGet(eq("http://platform.iov42/other/stuff"), any()))
                .thenReturn(new HttpBackendResponse("", null, 200, "{\"requestId\": \"abc\"}"));

        HttpHostWrapper wrapper = new HttpHostWrapper(mockBackend, "http://platform.iov42");
        RequestInfoResponse requestInfoResponse =
                wrapper.executePut("/api/v1/requests/abc", "body".getBytes(StandardCharsets.UTF_8), null, RequestInfoResponse.class).join();

        assertEquals("abc", requestInfoResponse.getRequestId());
    }
}
