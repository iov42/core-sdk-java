package com.iov42.solutions.core.sdk.model;

import com.iov42.solutions.core.sdk.model.requests.BaseRequest;

public class PostHeadersData extends BaseRequest {

    private String authenticationBase64Url;

    private String authorisationsBase64Url;

    private String claims;

    public String getAuthenticationBase64Url() {
        return authenticationBase64Url;
    }

    public void setAuthenticationBase64Url(String authenticationBase64Url) {
        this.authenticationBase64Url = authenticationBase64Url;
    }

    public String getAuthorisationsBase64Url() {
        return authorisationsBase64Url;
    }

    public void setAuthorisationsBase64Url(String authorisationsBase64Url) {
        this.authorisationsBase64Url = authorisationsBase64Url;
    }

    public String getClaims() {
        return claims;
    }

    public void setClaims(String claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return "PostHeadersData{" +
                "authenticationBase64Url='" + authenticationBase64Url + '\'' +
                ", authorisationsBase64Url='" + authorisationsBase64Url + '\'' +
                ", claims='" + claims + '\'' +
                "} " + super.toString();
    }
}
