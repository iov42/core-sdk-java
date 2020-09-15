package com.iov42.solutions.core.sdk.errors;

public class BasePlatformError {

    private int errorCode;

    private String errorType;

    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{ errorCode=" + errorCode +
                ", errorType=" + errorType +
                ", message=" + message + " }";
    }
}
