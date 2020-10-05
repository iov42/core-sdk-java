package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class AddDelegateRequest extends BaseRequest {

    private final String delegateIdentityId;

    public AddDelegateRequest(String delegateIdentityId) {
        super();
        this.delegateIdentityId = delegateIdentityId;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    @Override
    public String toString() {
        return "AddDelegateRequest{" +
                "delegateIdentityId='" + delegateIdentityId + '\'' +
                "} " + super.toString();
    }
}
