package com.iov42.solutions.core.sdk.crypto.spi;

import com.iov42.solutions.core.sdk.model.ProtocolType;

import java.security.KeyPair;

/**
 * Cryptography backend used by the iov42 core library.
 *
 */
public interface CryptoBackend {
    /**
     * Signs the supplied data with the privateKey (which have to correspond to the protocolType).
     *
     * @param protocolType the used signature protocol
     * @param privateKey   the private key
     * @param data         data to sign
     * @return the signature bytes
     */
    byte[] sign(ProtocolType protocolType, byte[] privateKey, byte[] data);

    /**
     * Verifies a supplied signature with a give public key (which have to correspond to the protocolType).
     *
     * @param protocolType the used signature protocol
     * @param publicKey    the public key
     * @param data         data that was signed
     * @param signature    the signature
     * @return {@code True} if the signature matches, {@code False} otherwise.
     */
    boolean verifySignature(ProtocolType protocolType, byte[] publicKey, byte[] data, byte[] signature);

    /**
     * Hashes the supplied data bytes with the SHA256 algorithm.
     *
     * @param data data to hash
     * @return the hash bytes
     */
    byte[] hash(byte[] data);

    /**
     * Generates a new random {@link KeyPair} of the supplied protocolType.
     *
     * @param protocolType protocol to be used
     * @return a new random {@link KeyPair}
     */
    KeyPair generateKeyPair(ProtocolType protocolType);
}
