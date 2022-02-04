package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendRequest;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpHostWrapperTest {

    private static List<String> list(String value) {
        List<String> l = new ArrayList<>();
        l.add(value);
        return l;
    }

    @Test
    void redirectShouldBeHandled() {
        HttpBackend mockBackend = mock(HttpBackend.class);
        Map<String, List<String>> headers = new HashMap<>();

        headers.put("Location", list("/other/location"));
        headers.put("Retry-After", list("1"));

        HttpBackendResponse response = new HttpBackendResponse(new HttpBackendRequest(HttpBackendRequest.Method.PUT, "", null), headers, 303, null);
        when(mockBackend.execute(TestHelper.put("http://platform.iov42/api/v1/requests/abc")))
                .thenReturn(CompletableFuture.completedFuture(response));

        HttpBackendResponse redirectResponse = new HttpBackendResponse(new HttpBackendRequest(HttpBackendRequest.Method.PUT, "", null), null, 200, "{\"requestId\": \"abc\"}");
        when(mockBackend.execute(TestHelper.put("http://platform.iov42/other/location")))
                .thenReturn(CompletableFuture.completedFuture(redirectResponse));

        HttpHostWrapper wrapper = new HttpHostWrapper(mockBackend, "http://platform.iov42");
        RequestInfoResponse requestInfoResponse =
                wrapper.executePut("/api/v1/requests/abc", "body".getBytes(StandardCharsets.UTF_8), null, RequestInfoResponse.class).join();

        assertEquals("abc", requestInfoResponse.getRequestId());
    }
}
