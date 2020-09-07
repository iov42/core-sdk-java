package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.CryptoBackend;
import com.iov42.solutions.core.sdk.model.ProtocolType;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class DefaultCryptoBackend implements CryptoBackend {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] sign(ProtocolType protocolType, byte[] privateKey, byte[] data) {
        try {
            KeyFactory keyFactory = getKeyFactory(protocolType);
            PrivateKey privateKeyInstance = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));

            Signature signature = getSignature(protocolType);
            signature.initSign(privateKeyInstance);
            signature.update(data);

            return signature.sign();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException ex) {
            throw new RuntimeException("Could not sign data.", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifySignature(ProtocolType protocolType, byte[] publicKey, byte[] data, byte[] signature) {

        try {
            KeyFactory keyFactory = getKeyFactory(protocolType);

            PublicKey publicKeyInstance = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));

            java.security.Signature signatureInstance = getSignature(protocolType);
            signatureInstance.initVerify(publicKeyInstance);
            signatureInstance.update(data);

            return signatureInstance.verify(signature);

        } catch (NoSuchAlgorithmException | SignatureException | NoSuchProviderException | InvalidKeySpecException | InvalidKeyException ex) {
            throw new RuntimeException("Could not verify signed data.", ex);
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
            throw new RuntimeException("Unable to create encoded hash for claim.", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPair generateKeyPair(ProtocolType protocolType) {
        KeyPair pair;
        KeyPairGenerator keyGen;
        try {
            switch (protocolType) {
                case SHA256WithRSA:
                    keyGen = KeyPairGenerator.getInstance("RSA");
                    keyGen.initialize(2048);
                    pair = keyGen.genKeyPair();

                    break;
                case SHA256WithECDSA:
                    keyGen = KeyPairGenerator.getInstance("EC");
                    keyGen.initialize(new ECGenParameterSpec("secp256k1"));
                    pair = keyGen.genKeyPair();
                    break;
                default:
                    throw new RuntimeException("Not Implemented");
            }
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
            throw new RuntimeException("Could not generate RSA key pair.", ex);
        }
        return pair;
    }

    private static KeyFactory getKeyFactory(ProtocolType protocolType) throws NoSuchProviderException, NoSuchAlgorithmException {
        switch (protocolType) {
            case SHA256WithECDSA:
                return KeyFactory.getInstance("EC");
            case SHA256WithRSA:
                return KeyFactory.getInstance("RSA");
            default:
                throw new RuntimeException("Unsupported ProtocolType");
        }
    }

    private static Signature getSignature(ProtocolType protocolType) throws NoSuchAlgorithmException, NoSuchProviderException {
        String algorithm = protocolType.name();
        return Signature.getInstance(algorithm);
    }
}
