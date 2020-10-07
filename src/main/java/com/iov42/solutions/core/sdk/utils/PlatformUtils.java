package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.PlatformClient;
import com.iov42.solutions.core.sdk.model.IovKeyPair;
import com.iov42.solutions.core.sdk.model.SignatureIOV;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlatformUtils {

    private PlatformUtils() {
        // static usage only
    }

    public static List<String> createClaimsHeaders(IovKeyPair keyPair, String body, Collection<String> claims) {
        Map<String, String> claimMap = claims.stream()
                .collect(Collectors.toMap(PlatformUtils::hashClaim, Function.identity()));

        List<String> headers = createPutHeaders(keyPair, body);
        headers.add(Constants.HEADER_IOV42_CLAIMS);
        headers.add(getEncodedHeaderValue(claimMap));
        return headers;
    }

    public static List<String> createEndorsementsHeaders(IovKeyPair keyPair, String body) {
        List<String> headers = createPutHeaders(keyPair, body);
        headers.add(Constants.HEADER_IOV42_CLAIMS);
        headers.add(Constants.ENDORSER_ONLY_CLAIM_HEADER_VALUE);
        return headers;
    }

    public static List<String> createGetHeaders(IovKeyPair keyPair, String payload) {
        SignatureIOV authenticationSignature = SecurityUtils.sign(keyPair, payload);

        List<String> headers = new ArrayList<>();
        headers.add(Constants.HEADER_AUTHENTICATION);
        headers.add(getEncodedHeaderValue(authenticationSignature));
        return headers;
    }

    public static List<String> createPutHeaders(IovKeyPair keyPair, String body) {
        SignatureIOV authorisationSignature = SecurityUtils.sign(keyPair, body);
        SignatureIOV authenticationSignature = SecurityUtils.sign(keyPair, authorisationSignature.getSignature());

        List<String> headers = new ArrayList<>();
        headers.add(Constants.HEADER_AUTHENTICATION);
        headers.add(getEncodedHeaderValue(authenticationSignature));
        headers.add(Constants.HEADER_AUTHORISATIONS);
        headers.add(getEncodedHeaderValue(List.of(authorisationSignature)));
        headers.add(Constants.HEADER_CONTENT_TYPE);
        headers.add(Constants.APPLICATION_JSON_VALUE);
        return headers;
    }

    public static String hashClaim(String claim) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(claim.getBytes(StandardCharsets.UTF_8));
            return SecurityUtils.encodeBase64(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unable to create encoded hash for claim.", ex);
        }
    }

    public static List<String> hashClaims(Stream<String> claims) {
        return claims.map(PlatformUtils::hashClaim).collect(Collectors.toList());
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
}
