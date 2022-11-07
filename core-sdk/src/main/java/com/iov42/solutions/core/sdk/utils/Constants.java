package com.iov42.solutions.core.sdk.utils;

/**
 * Holds constants used by the iov42 core library.
 */
public class Constants {

    private Constants() {}

    /**
     * Http header name for authentication.
     */
    public static final String HEADER_AUTHENTICATION = "X-IOV42-Authentication";

    /**
     * Http header name for authorisation.
     */
    public static final String HEADER_AUTHORISATIONS = "X-IOV42-Authorisations";

    /**
     * Http header name for content type.
     */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    /**
     * Http header name for claims.
     */
    public static final String HEADER_IOV42_CLAIMS = "X-IOV42-Claims";

    /**
     * Application type for json content.
     */
    public static final String APPLICATION_JSON_VALUE = "application/json";

    /**
     * Empty claims value (Base64 encoded "{}").
     */
    public static final String ENDORSER_ONLY_CLAIM_HEADER_VALUE = "e30=";
}
