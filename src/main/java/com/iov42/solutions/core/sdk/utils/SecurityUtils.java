package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.model.SignatoryIOV;
import com.iov42.solutions.core.sdk.model.SignatureIOV;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class SecurityUtils {

    private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();

    private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();

    public static byte[] decodeBase64(String data) {
        return BASE64_DECODER.decode(data);
    }

    public static String encodeBase64(byte[] data) {
        return BASE64_ENCODER.encodeToString(data);
    }

    public static KeyPair generateKeyPair() {
        KeyPair pair;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            pair = keyGen.genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Could not generate RSA key pair.", ex);
        }

        return pair;
    }

    public static SignatureIOV sign(SignatoryIOV signatory, String data) {

        if (Objects.nonNull(signatory.getDelegatorIdentityId())) {
            // this is a signature on behalf of the delegator
            return new SignatureIOV(
                    signatory.getDelegatorIdentityId(),
                    signatory.getIdentityId(),
                    signatory.getProtocolId(),
                    sign(signatory.getProtocolId(), signatory.getPrivateKey(), data)
            );
        }
        return new SignatureIOV(
                signatory.getIdentityId(),
                signatory.getProtocolId(),
                sign(signatory.getProtocolId(), signatory.getPrivateKey(), data)
        );
    }

    public static String sign(String algorithm, byte[] privateKey, String data) {
        try {
            KeyFactory keyFactory = getKeyFactory(algorithm);
            PrivateKey privateKeyInstance = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));

            Signature signature = getSignature(algorithm);
            signature.initSign(privateKeyInstance);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            return BASE64_ENCODER.encodeToString(signature.sign());
        } catch (NoSuchProviderException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException ex) {
            throw new RuntimeException("Could not sign data.", ex);
        }
    }

    private static String getKeyAlgorithm(String algorithm) {
        int idx = algorithm.indexOf("With");
        if (idx > -1) {
            return algorithm.substring(idx + 4);
        }
        return algorithm;
    }

    private static KeyFactory getKeyFactory(String algorithm) throws NoSuchProviderException, NoSuchAlgorithmException {
        String keyAlgorithm = getKeyAlgorithm(algorithm);
        if ("RSA".equals(keyAlgorithm)) {
            return KeyFactory.getInstance("RSA");
        }
        return KeyFactory.getInstance(keyAlgorithm, "BC");
    }

    private static Signature getSignature(String algorithm) throws NoSuchAlgorithmException, NoSuchProviderException {
        String keyAlgorithm = getKeyAlgorithm(algorithm);
        if ("RSA".equals(keyAlgorithm)) {
            return Signature.getInstance(algorithm);
        }
        return Signature.getInstance(algorithm, "BC");
    }
}
