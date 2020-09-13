package com.iov42.solutions.core.sdk.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpUtilsTest {

    @Test
    @DisplayName("Basic http GET test")
    public void testGET() throws InterruptedException, IOException, URISyntaxException {
        String url = "https://postman-echo.com/get?param1=test1&param2=test2";
        HttpResponse<String> response = HttpUtils.get(url);
        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    @DisplayName("Basic http async POST test")
    public void testPOST() throws URISyntaxException, ExecutionException, InterruptedException {
        String url = "https://postman-echo.com/post";
        String body = "This is expected to be sent back as part of response body.";
        CompletableFuture<HttpResponse<String>> future = HttpUtils.postAsync(url, body.getBytes(StandardCharsets.UTF_8));
        assertNotNull(future);
        HttpResponse<String> response = future.get();
        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }
}
