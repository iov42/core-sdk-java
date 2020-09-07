package com.iov42.solutions.core.sdk.model;

import java.util.List;

public class AuthorisedRequest extends BaseRequest {

    private AuthenticationData authentication;

    private List<AuthorisationData> authorisations;

    private String payload;

    public AuthenticationData getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationData authentication) {
        this.authentication = authentication;
    }

    public List<AuthorisationData> getAuthorisations() {
        return authorisations;
    }

    public void setAuthorisations(List<AuthorisationData> authorisations) {
        this.authorisations = authorisations;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "AuthorisedRequest{" +
                "authentication=" + authentication +
                ", authorisations=" + authorisations +
                ", payload='" + payload + '\'' +
                "} " + super.toString();
    }
}
