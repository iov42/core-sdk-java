package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.CryptoBackend;
import com.iov42.solutions.core.sdk.model.CryptoBackendException;
import com.iov42.solutions.core.sdk.model.ProtocolType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class DefaultCryptoBackend implements CryptoBackend {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String SYSTEM_PROPERTY_EC_SPEC_NAME = "iov42.core.sdk.crypto-backend.ec-spec-name";

    private final String ecSpecName;

    public DefaultCryptoBackend() {
        String ecPropName = System.getProperty(SYSTEM_PROPERTY_EC_SPEC_NAME);
        ecSpecName = ecPropName != null ? ecPropName : "secp256r1";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] sign(ProtocolType protocolType, byte[] privateKey, byte[] data) {
        try {
            Provider provider = getProviderFor(protocolType);

            KeyFactory keyFactory = getKeyFactory(protocolType, provider);
            PrivateKey privateKeyInstance = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));

            Signature signature = getSignature(protocolType, provider);
            signature.initSign(privateKeyInstance);
            signature.update(data);

            return signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException ex) {
            throw new CryptoBackendException("Could not sign data.", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifySignature(ProtocolType protocolType, byte[] publicKey, byte[] data, byte[] signature) {

        try {
            Provider provider = getProviderFor(protocolType);

            KeyFactory keyFactory = getKeyFactory(protocolType, provider);

            PublicKey publicKeyInstance = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));

            java.security.Signature signatureInstance = getSignature(protocolType, provider);
            signatureInstance.initVerify(publicKeyInstance);
            signatureInstance.update(data);

            return signatureInstance.verify(signature);

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException ex) {
            throw new CryptoBackendException("Could not verify signed data.", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] hash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoBackendException("Unable to create encoded hash for claim.", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPair generateKeyPair(ProtocolType protocolType) {
        Provider provider = getProviderFor(protocolType);
        KeyPair pair;
        KeyPairGenerator keyGen;
        try {
            switch (protocolType) {
                case SHA256WithECDSA:
                    keyGen = KeyPairGenerator.getInstance("EC", provider);
                    keyGen.initialize(new ECGenParameterSpec(ecSpecName));
                    pair = keyGen.genKeyPair();
                    break;
                case SHA256WithRSA:
                    keyGen = KeyPairGenerator.getInstance("RSA", provider);
                    keyGen.initialize(2048);
                    pair = keyGen.genKeyPair();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + protocolType);
            }
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
            throw new CryptoBackendException("Could not generate RSA key pair.", ex);
        }
        return pair;
    }

    private static KeyFactory getKeyFactory(ProtocolType protocolType, Provider provider) throws NoSuchAlgorithmException {
        switch (protocolType) {
            case SHA256WithECDSA:
                return KeyFactory.getInstance("EC", provider);
            case SHA256WithRSA:
                return KeyFactory.getInstance("RSA", provider);
            default:
                throw new IllegalStateException("Unexpected value: " + protocolType);
        }
    }

    private static Signature getSignature(ProtocolType protocolType, Provider provider) throws NoSuchAlgorithmException {
        String algorithm = protocolType.name();
        return Signature.getInstance(algorithm, provider);
    }

    private static Provider getProviderFor(ProtocolType protocolType) {
        switch (protocolType) {
            case SHA256WithECDSA:
                return Security.getProvider("BC");
            case SHA256WithRSA:
                return Security.getProvider("SunRsaSign");
            default:
                throw new IllegalStateException("Unexpected value: " + protocolType);
        }
    }
}
