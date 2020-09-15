package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.PlatformClient;
import com.iov42.solutions.core.sdk.model.KeyPairWrapper;
import com.iov42.solutions.core.sdk.model.SignatoryIOV;
import com.iov42.solutions.core.sdk.model.SignatureIOV;
import com.iov42.solutions.core.sdk.model.requests.BaseRequest;
import com.iov42.solutions.core.sdk.model.responses.AsyncRequestInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlatformUtils {

    public static final String AUTHENTICATION = "X-IOV42-Authentication";

    public static final String AUTHORISATIONS = "X-IOV42-Authorisations";

    private PlatformUtils() {
        // static usage only
    }

    public static List<String> createGetHeaders(KeyPairWrapper keyPair, String payload) {

        SignatoryIOV signatory = new SignatoryIOV(keyPair.getIdentityId(), keyPair.getProtocolId().name(), keyPair.getPrivateKey());

        SignatureIOV authenticationSignature = SecurityUtils.sign(signatory, payload);

        List<String> headers = new ArrayList<>();
        headers.add(AUTHENTICATION);
        headers.add(PlatformUtils.getEncodedHeaderValue(authenticationSignature));

        return headers;
    }

    public static List<String> createHeaders(KeyPairWrapper keyPair, String body) {

        SignatoryIOV signatory = new SignatoryIOV(keyPair.getIdentityId(), keyPair.getProtocolId().name(), keyPair.getPrivateKey());

        SignatureIOV authorisationSignature = SecurityUtils.sign(signatory, body);
        SignatureIOV authenticationSignature = SecurityUtils.sign(signatory, authorisationSignature.getSignature());

        List<String> headers = new ArrayList<>();
        headers.add(AUTHENTICATION);
        headers.add(PlatformUtils.getEncodedHeaderValue(authenticationSignature));
        headers.add(AUTHORISATIONS);
        headers.add(PlatformUtils.getEncodedHeaderValue(List.of(authorisationSignature)));
        headers.add("Content-Type");
        headers.add("application/json");

        return headers;
    }

    public static AsyncRequestInfo waitForRequest(PlatformClient client, BaseRequest requestResponse) throws Exception {
        if (requestResponse == null || requestResponse.getRequestId() == null || requestResponse.getRequestId().trim().length() == 0) {
            return null;
        }
        AsyncRequestInfo result;
        while (true) {
            result = client.getRequest(requestResponse.getRequestId());
            if (result.hasFinished()) {
                break;
            }
            // wait on the thread for a second
            TimeUnit.MILLISECONDS.sleep(100);
        }
        return result;
    }

    private static String getEncodedHeaderValue(Object value) {
        return SecurityUtils.encodeBase64(JsonUtils.toJson(value).getBytes(StandardCharsets.UTF_8));
    }
}
