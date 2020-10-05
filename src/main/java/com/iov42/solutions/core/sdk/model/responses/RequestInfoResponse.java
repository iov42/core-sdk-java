package com.iov42.solutions.core.sdk.model.responses;

import java.util.Arrays;

/**
 * Response object of the {@link com.iov42.solutions.core.sdk.PlatformClient#getRequest(String)}
 */
public class RequestInfoResponse extends BaseResponse {

    private String requestId;

    private String[] resources;

    private RequestInfoResponse() {
        // needed for Jackson parser
    }

    public RequestInfoResponse(String proof, String requestId, String[] resources) {
        super(proof);
        this.requestId = requestId;
        this.resources = resources;
    }

    public String getRequestId() {
        return requestId;
    }

    public String[] getResources() {
        return resources;
    }

    public boolean hasFinished() {
        return resources != null;
    }

    @Override
    public String toString() {
        return "RequestInfoResponse{" +
                "requestId='" + requestId + '\'' +
                ", resources=" + Arrays.toString(resources) +
                "} " + super.toString();
    }
}
