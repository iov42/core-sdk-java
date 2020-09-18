package com.iov42.solutions.core.sdk.model.requests.post;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class AddDelegateRequest extends BaseRequest {

    private String delegateIdentityId;

    public AddDelegateRequest() {
        super();
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }

    public void setDelegateIdentityId(String delegateIdentityId) {
        this.delegateIdentityId = delegateIdentityId;
    }

    @Override
    public String toString() {
        return "AddDelegateRequest{" +
                "delegateIdentityId='" + delegateIdentityId + '\'' +
                "} " + super.toString();
    }
}
