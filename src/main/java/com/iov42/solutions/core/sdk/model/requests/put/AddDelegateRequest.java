package com.iov42.solutions.core.sdk.model.requests.put;

public class AddDelegateRequest extends BasePutRequest {

    private final String delegateIdentityId;

    public AddDelegateRequest(String delegateIdentityId) {
        super(TransactionType.AddDelegateRequest);
        this.delegateIdentityId = delegateIdentityId;
    }

    public AddDelegateRequest(String requestId, String delegateIdentityId) {
        super(requestId, TransactionType.AddDelegateRequest);
        this.delegateIdentityId = delegateIdentityId;
    }

    public String getDelegateIdentityId() {
        return delegateIdentityId;
    }
}
