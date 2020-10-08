package com.iov42.solutions.core.sdk.model.requests.put;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public abstract class BasePutRequest extends BaseRequest {

    private final TransactionType _type;

    protected BasePutRequest(TransactionType _type) {
        super();
        this._type = _type;
    }

    protected BasePutRequest(String requestId, TransactionType _type) {
        super(requestId);
        this._type = _type;
    }

    public TransactionType get_type() {
        return _type;
    }
}
