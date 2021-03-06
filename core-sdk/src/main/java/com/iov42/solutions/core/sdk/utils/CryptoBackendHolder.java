package com.iov42.solutions.core.sdk.utils;

import com.iov42.solutions.core.sdk.CryptoBackend;

import java.lang.reflect.Constructor;

/**
 * Sets the {@link CryptoBackend} to be used within the iov42 core library.
 * This class provides static access to an instance of {@link CryptoBackend} for convenience.
 * <p>
 * To specify a custom {@link CryptoBackend} you must specify either a fully qualified classname
 * to a concrete implementation of {@link CryptoBackend} or you can set it later with {@link #setBackend(CryptoBackend)}.
 */
public class CryptoBackendHolder {

    public static final String SYSTEM_PROPERTY = "iov42.core.sdk.crypto-backend";
    private static String backendName = System.getProperty(SYSTEM_PROPERTY);
    private static CryptoBackend backend;

    static {
        initialize();
    }

    private static void initialize() {
        if (StringUtils.isEmpty(backendName)) {
            // instantiate default backend
            backend = new DefaultCryptoBackend();
        } else {
            // try to instantiate custom backend
            try {
                Class<?> clazz = Class.forName(backendName);
                Constructor<?> backendConstructor = clazz.getConstructor();
                backend = (CryptoBackend) backendConstructor.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Could not instantiate custom CryptoBackend.", ex);
            }
        }
    }

    /**
     * Returns an instance of {@link CryptoBackend} used by the iov42 core library.
     *
     * @return an instance of {@link CryptoBackend} used by the iov42 core library
     */
    public static CryptoBackend getBackend() {
        return backend;
    }

    /**
     * Sets an instance of {@link CryptoBackend} used by the iov42 core library.
     *
     * @param backend instance of {@link CryptoBackend} used by the iov42 core library
     */
    public static void setBackend(CryptoBackend backend) {
        CryptoBackendHolder.backend = backend;
    }
}
