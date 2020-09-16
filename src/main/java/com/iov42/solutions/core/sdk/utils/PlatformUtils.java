package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.PlatformClient;
import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.SignatoryIOV;
import com.iov42.solutions.core.sdk.model.SignatureIOV;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlatformUtils {

    public static final String APPLICATION_JSON_VALUE = "application/json";

    public static final String HEADER_AUTHENTICATION = "X-IOV42-Authentication";

    public static final String HEADER_AUTHORISATIONS = "X-IOV42-Authorisations";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final String HEADER_IOV42_CLAIMS = "X-IOV42-Claims";

    private PlatformUtils() {
        // static usage only
    }

    public static List<String> createGetHeaders(IovKeyPair keyPair, String payload) {

        SignatoryIOV signatory = new SignatoryIOV(keyPair.getIdentityId(), keyPair.getProtocolId().name(), keyPair.getPrivateKey());

        SignatureIOV authenticationSignature = SecurityUtils.sign(signatory, payload);

        List<String> headers = new ArrayList<>();
        headers.add(HEADER_AUTHENTICATION);
        headers.add(PlatformUtils.getEncodedHeaderValue(authenticationSignature));

        return headers;
    }

    public static List<String> createPostHeaders(IovKeyPair keyPair, String body) {

        SignatoryIOV signatory = new SignatoryIOV(keyPair.getIdentityId(), keyPair.getProtocolId().name(), keyPair.getPrivateKey());

        SignatureIOV authorisationSignature = SecurityUtils.sign(signatory, body);
        SignatureIOV authenticationSignature = SecurityUtils.sign(signatory, authorisationSignature.getSignature());

        List<String> headers = new ArrayList<>();
        headers.add(HEADER_AUTHENTICATION);
        headers.add(PlatformUtils.getEncodedHeaderValue(authenticationSignature));
        headers.add(HEADER_AUTHORISATIONS);
        headers.add(PlatformUtils.getEncodedHeaderValue(List.of(authorisationSignature)));
        headers.add(HEADER_CONTENT_TYPE);
        headers.add(APPLICATION_JSON_VALUE);

        return headers;
    }

    public static List<String> createPostHeadersWithClaims(IovKeyPair keyPair, String body, List<String> claims) {
        List<String> headers = createPostHeaders(keyPair, body);

        Map<String, String> claimMap = claims.stream()
                .collect(Collectors.toMap(PlatformUtils::getEncodedClaimHash, Function.identity()));

        headers.add(HEADER_IOV42_CLAIMS);
        headers.add(getEncodedHeaderValue(claimMap));

        return headers;
    }

    public static String getEncodedClaimHash(String plainClaim) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainClaim.getBytes(StandardCharsets.UTF_8));
            return SecurityUtils.encodeBase64(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unable to create encoded hash for claim.", ex);
        }
    }

    public static RequestInfoResponse waitForRequest(String requestId, PlatformClient client, int reTryAfter) throws Exception {
        if (requestId == null || requestId.trim().length() == 0) {
            return null;
        }
        int attempts = 10;
        RequestInfoResponse result = null;
        while (true) {
            if (attempts == 0) {
                break;
            }
            result = client.getRequest(requestId);
            if (Objects.nonNull(result) && result.hasFinished()) {
                break;
            }
            attempts--;
            // wait on the thread for a second
            TimeUnit.SECONDS.sleep(reTryAfter);
        }
        return result;
    }

    private static String getEncodedHeaderValue(Object value) {
        return SecurityUtils.encodeBase64(JsonUtils.toJson(value).getBytes(StandardCharsets.UTF_8));
    }

    public List<String> hashClaims(List<String> claims) {
        return claims.stream().map(PlatformUtils::getEncodedClaimHash).collect(Collectors.toList());
    }
}
