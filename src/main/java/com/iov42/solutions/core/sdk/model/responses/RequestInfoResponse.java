package com.iov42.solutions.core.sdk.model.responses;

import java.util.List;

/**
 * Response object of the {@link com.iov42.solutions.core.sdk.PlatformClient#getRequest(String)}
 */
public class RequestInfoResponse extends BaseResponse {

    private RequestInfoResponse() {
        // needed for Jackson parser
    }

    public RequestInfoResponse(String proof, String requestId, List<String> resources) {
        super(proof);
        setRequestId(requestId);
        setResources(resources);
    }
}
