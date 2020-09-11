package com.iov42.solutions.core.sdk.model;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class GetHeadersData extends BaseRequest {

    private String authenticationBase64Url;

    public String getAuthenticationBase64Url() {
        return authenticationBase64Url;
    }

    public void setAuthenticationBase64Url(String authenticationBase64Url) {
        this.authenticationBase64Url = authenticationBase64Url;
    }

    @Override
    public String toString() {
        return "GetHeadersData{" +
                "authenticationBase64Url='" + authenticationBase64Url + '\'' +
                "} " + super.toString();
    }
}
