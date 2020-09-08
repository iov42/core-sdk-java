package com.iov42.solutions.core.sdk.http;

import com.google.gson.Gson;
import com.iov42.solutions.core.sdk.errors.HttpClientError;
import com.iov42.solutions.core.sdk.errors.HttpServerError;
import com.iov42.solutions.core.sdk.model.HealthChecks;
import com.iov42.solutions.core.sdk.model.NodeInfo;

import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * The PlatformClient is entry point to access iov42 platform. It is providing ways to:
 * <p>
 * Create and access identities
 * Manipulate assets
 */
public class PlatformClient {

    public static final String DEFAULT_URL = "https://api.sandbox.iov42.dev/api";

    private final Gson gson;

    private String url;

    private String version = "v1";

    public PlatformClient(String url) {
        if (Objects.isNull(url) || url.trim().length() == 0) {
            throw new IllegalArgumentException("Invalid URL argument");
        }
        this.url = url;
        if (this.url.endsWith("/")) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }

        this.gson = new Gson();
    }

    public PlatformClient(String url, String version) {
        this(url);
        this.version = version;
    }

    /**
     * Retrieves information about the node's health
     * See api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1healthchecks/get
     */
    public Optional<HealthChecks> getHealthChecks() throws Exception {
        HttpResponse<String> response = PlatformUtils.get(url + "/" + version + "/healthchecks");
        return Optional.of(handleResponse(response, HealthChecks.class));
    }

    /**
     * Retrieves information about a node in the iov42 platform
     * See the api specs at:
     * https://api.sandbox.iov42.dev/api/v1/apidocs/redoc.html#tag/operations/paths/~1node-info/get
     */
    public Optional<NodeInfo> getNodeInfo() throws Exception {
        HttpResponse<String> response = PlatformUtils.get(url + "/" + version + "/node-info");
        return Optional.of(handleResponse(response, NodeInfo.class));
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) throws HttpClientError, HttpServerError {
        if (response.statusCode() >= 400 && response.statusCode() < 500) {
            throw new HttpClientError(response.body(), response.statusCode());
        } else if (response.statusCode() >= 500) {
            throw new HttpServerError(response.body(), response.statusCode());
        }
        return gson.fromJson(response.body(), clazz);
    }
}
