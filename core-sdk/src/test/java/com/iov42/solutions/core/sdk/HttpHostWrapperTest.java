package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.http.HttpBackend;
import com.iov42.solutions.core.sdk.http.HttpBackendRequest;
import com.iov42.solutions.core.sdk.http.HttpBackendResponse;
import com.iov42.solutions.core.sdk.model.PlatformError;
import com.iov42.solutions.core.sdk.model.PlatformErrorException;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void convertResponseShouldThrowOnErrorResponse() {
        HttpBackendResponse response = new HttpBackendResponse(null, null, 400,
                "{\"errors\":[{\"errorCode\":4711,\"errorType\":\"FancyType\",\"message\":\"Fancy Error Message\"}," +
                        "{\"errorCode\":4712,\"errorType\":\"OtherFancyType\",\"message\":\"Other Fancy Error Message\"}]," +
                        "\"requestId\":\"ABC\",\"proof\":\"/api/v1/proofs/XYZ\"}");
        PlatformErrorException ex = assertThrows(PlatformErrorException.class, () -> HttpHostWrapper.convertResponse(response, RequestInfoResponse.class));

        assertEquals(400, ex.getStatusCode());
        assertEquals(2, ex.getErrorResponse().getErrors().size());
        PlatformError error = ex.getErrorResponse().getErrors().get(0);

        assertEquals(4711, error.getErrorCode());
        assertEquals("FancyType", error.getErrorType());
        assertEquals("Fancy Error Message", error.getMessage());

        assertEquals("Status code: 400\n" +
                "* code: 4711, type: FancyType, 'Fancy Error Message'\n" +
                "* code: 4712, type: OtherFancyType, 'Other Fancy Error Message'\n", ex.getMessage());
    }
}
