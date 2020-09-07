package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

/**
 * Structure represents information about a request.
 */
public class RequestInfoResponse extends BaseResponse {

    private String requestId;

    private List<String> resources;

    private RequestInfoResponse() {
    }

    RequestInfoResponse(String proof, String requestId, List<String> resources) {
        super(proof);
        this.requestId = requestId;
        this.resources = resources;
    }

    /**
     * Returns the requests identifier.
     *
     * @return the requests identifier
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns a {@link List} of resource links involved in the request.
     *
     * @return a {@link List} of resource links involved in the request
     */
    public List<String> getResources() {
        return resources;
    }

    /**
     * Returns {@code True} if the request is already finished (successful) or {@code False} otherwise.
     *
     * @return {@code True} if the request is already finished (successful) or {@code False} otherwise
     */
    public boolean hasFinished() {
        return resources != null;
    }
}
