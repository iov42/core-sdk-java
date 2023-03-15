package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.crypto.CryptoBackendHolder;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.utils.serialization.*;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A collection of utility functions used by the iov42 core library.
 */
public class PlatformUtils {

    private PlatformUtils() {
        // static usage only
    }

    private static class Base64DecoderHolder {
        public static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();
    }

    private static class Base64EncoderHolder {
        public static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    }

    /**
     * Decodes a base64 encoded string into bytes.
     *
     * @param data the base64 encoded {@code String}
     * @return the decodes {@code byte} array
     */
    public static byte[] decodeBase64(String data) {
        return Base64DecoderHolder.BASE64_DECODER.decode(data);
    }

    /**
     * Encodes a {@code byte} array into a base64 string (without padding).
     *
     * @param data the {@link byte} array
     * @return the base64 encoded {@code String}
     */
    public static String encodeBase64(byte[] data) {
        return Base64EncoderHolder.BASE64_ENCODER.encodeToString(data);
    }

    /**
     * Creates a {@link SignatureInfo} object based on the supplied {@link SignatoryInfo} and data to sign.
     *
     * @param signatoryInfo {@link SignatoryInfo} used to sign
     * @param data          data to sign (UTF8 bytes will be signed)
     * @return a new {@link SignatureInfo}
     */
    public static SignatureInfo sign(SignatoryInfo signatoryInfo, String data) {

        if (Objects.nonNull(signatoryInfo.getDelegateIdentityId())) {
            // this is a signature on behalf of the delegator
            return new SignatureInfo(
                    signatoryInfo.getDelegateIdentityId(),
                    signatoryInfo.getIdentityId(),
                    signatoryInfo.getProtocolId(),
                    sign(signatoryInfo.getProtocolId(), signatoryInfo.getPrivateKey(), data)
            );
        }
        return new SignatureInfo(
                signatoryInfo.getIdentityId(),
                signatoryInfo.getProtocolId(),
                sign(signatoryInfo.getProtocolId(), signatoryInfo.getPrivateKey(), data)
        );
    }

    /**
     * Create a base64 encoded signature of data with the given privateKey and protocolType.
     *
     * @param protocolType Signing protocol to be used
     * @param privateKey   private key used for the signature (must correspond the protocolType)
     * @param data         data to sign (UTF8 bytes will be signed)
     * @return a base64 signature string
     */
    public static String sign(ProtocolType protocolType, byte[] privateKey, String data) {
        return encodeBase64(CryptoBackendHolder.getBackend().sign(protocolType, privateKey, data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Verifies a supplied signature with a give public key (which have to correspond to the protocolType).
     *
     * @param protocolType the used signature protocol
     * @param publicKey    the public key
     * @param data         original data (UTF8 bytes will be taken)
     * @param signature    base64 encoded signature of data
     * @return {@code True} if the signature matches, {@code False} otherwise.
     */
    public static boolean verifySignature(ProtocolType protocolType, byte[] publicKey, String data, String signature) {
        return CryptoBackendHolder.getBackend().verifySignature(protocolType, publicKey, data.getBytes(StandardCharsets.UTF_8), decodeBase64(signature));
    }

    /**
     * Verifies an endorsement against {@link PublicCredentials} of an identity.
     *
     * @param endorsement         the endorsement (i.e. the signature of {subjectId};{subjectTypeId};{hashedClaim} - mind the ";" separator)
     * @param hashedClaim         the hashed claim
     * @param subjectTypeId       the subject type identifier
     * @param subjectId           the subject identifier
     * @param endorserCredentials the {@link PublicCredentials} of the endorser
     * @return {@code True} if the endorsement could be verfied, {@code False} otherwise.
     */
    public static boolean verifyEndorsement(String endorsement, String hashedClaim, String subjectTypeId, String subjectId, PublicCredentials endorserCredentials) {
        String data = subjectId + ";" + subjectTypeId + ";" + hashedClaim;
        return verifySignature(endorserCredentials.getProtocolId(), decodeBase64(endorserCredentials.getKey()), data, endorsement);
    }

    /**
     * Verifies an endorsement against {@link PublicCredentials} of an identity.
     *
     * @param endorsement         the endorsement (i.e. the signature of {subjectId};{hashedClaim} - mind the ";" separator)
     * @param hashedClaim         the hashed claim
     * @param subjectId           the subject identifier
     * @param endorserCredentials the {@link PublicCredentials} of the endorser
     * @return {@code True} if the endorsement could be verfied, {@code False} otherwise.
     */
    public static boolean verifyEndorsement(String endorsement, String hashedClaim, String subjectId, PublicCredentials endorserCredentials) {
        String data = subjectId + ";" + hashedClaim;
        return verifySignature(endorserCredentials.getProtocolId(), decodeBase64(endorserCredentials.getKey()), data, endorsement);
    }

    /**
     * Creates a list of http headers required for a get request to the iov42 platform.
     *
     * @param signatoryInfo {@link SignatoryInfo} used to sign the request
     * @param path          the path to sign
     * @return a map of http headers for a get request
     */
    public static Map<String, List<String>> createGetHeaders(SignatoryInfo signatoryInfo, String path) {
        SignatureInfo authenticationSignature = sign(signatoryInfo, path);

        Map<String, List<String>> headers = new HashMap<>();
        headers.put(Constants.HEADER_AUTHENTICATION, Collections.singletonList(getEncodedHeaderValue(authenticationSignature)));
        return headers;
    }

    /**
     * Creates a list of http headers required for a put request to the iov42 platform.
     *
     * @param authenticationInfo {@link SignatoryInfo} used to sign the authentication signature
     * @param authorisations     list of {@link SignatureInfo} required for the authentication signature
     * @return a list of http headers for a put request
     */
    public static List<String> createPutHeaders(SignatoryInfo authenticationInfo, List<SignatureInfo> authorisations) {

        SignatureInfo authenticationSignature = sign(authenticationInfo,
                authorisations.stream().map(SignatureInfo::getSignature).collect(Collectors.joining(";")));

        List<String> headers = new ArrayList<>();
        headers.add(Constants.HEADER_AUTHENTICATION);
        headers.add(getEncodedHeaderValue(authenticationSignature));
        headers.add(Constants.HEADER_AUTHORISATIONS);
        headers.add(getEncodedHeaderValue(authorisations));
        headers.add(Constants.HEADER_CONTENT_TYPE);
        headers.add(Constants.APPLICATION_JSON_VALUE);
        return headers;
    }

    /**
     * Generates a new random {@link KeyPair} based on the supplied {@link ProtocolType}.
     *
     * @param protocolType the {@link ProtocolType}
     * @return a new random {@link KeyPair}
     */
    public static KeyPair generateKeyPair(ProtocolType protocolType) {
        return CryptoBackendHolder.getBackend().generateKeyPair(protocolType);
    }

    /**
     * Generates a base64 encoded String of hash of a claim String. Currently SHA256 as hashing algorithm is used.
     *
     * @param claim the claim to be hashed
     * @return a base64 encoded hash
     */
    public static String hashClaim(String claim) {
        return encodeBase64(CryptoBackendHolder.getBackend().hash(claim.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Encodes a value to be passed as value in a header for requests to the iov42 platform.
     *
     * @param value object to be encoded
     * @return a base64 encoded String representation of the value
     */
    public static String getEncodedHeaderValue(Object value) {
        return encodeBase64(JsonUtils.toJson(value).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Create a {@link Map} of a {@link Claims} instance with the key being the hash of a claim and the value the plain claim text.
     *
     * @param claims the {@link Claims} instance
     * @return a {@link Map} (Key the has of a claim and Value the plain text)
     */
    public static Map<String, String> claimMap(Claims claims) {
        return claims.getPlainClaims().stream().collect(Collectors.toMap(PlatformUtils::hashClaim, v -> v));
    }

    static final Pattern PATTERN_REQUEST_ID = Pattern.compile("(\"requestId\"\\s*:\\s*\")(?<id>.*?)(?:\")");
    static final Pattern PATTERN_TYPE = Pattern.compile("(\"_type\"\\s*:\\s*\")(?<type>.*?)(?:\")");

    /**
     *
     * @param body
     * @return the requestId or null if nothing was found
     */
    public static String getRequestId(String body) {
        Matcher m = PATTERN_REQUEST_ID.matcher(body);
        return m.find() ? m.group("id") : null;
    }

    /**
     *
     * @param body
     * @return the type of the body or null if nothing was found
     */
    public static String getType(String body) {
        Matcher m = PATTERN_TYPE.matcher(body);
        return m.find() ? m.group("type") : null;
    }

    /**
     * Copies an exiting request and mutates the requestId.
     *
     * @param body
     * @param requestId
     * @return muted copy of the body
     */
    public static String mutateRequestId(String body, String requestId) {
        // replace request id
        return PATTERN_REQUEST_ID.matcher(body).replaceFirst("$1" + requestId + "$3");
    }
}
