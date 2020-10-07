package com.iov42.solutions.core.sdk.model.requests.put;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

import java.util.Objects;

public abstract class BasePutRequest extends BaseRequest {

    private TransactionType _type;

    public BasePutRequest(TransactionType _type) {
        super();
        this._type = _type;
    }

    public BasePutRequest(String requestId, TransactionType _type) {
        super(requestId);
        this._type = _type;
    }

    public TransactionType get_type() {
        return _type;
    }

    public void set_type(TransactionType _type) {
        this._type = _type;
    }

    public boolean hasType() {
        return Objects.nonNull(_type);
    }
}
