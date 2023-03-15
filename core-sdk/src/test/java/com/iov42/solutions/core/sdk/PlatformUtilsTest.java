package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class PlatformUtilsTest {

    @Test
    void arrayShouldBeBase64EncodedAndDecoded() {

        // prepare
        byte[] array = new byte[]{1, 2, 3, 4};

        // act
        String str = PlatformUtils.encodeBase64(array);
        byte[] other = PlatformUtils.decodeBase64(str);

        // assert
        assertArrayEquals(array, other);
    }

    @Test
    void generateAndUseRSAKeyPairShouldWork() {
        // prepare and act
        KeyPair keyPair = PlatformUtils.generateKeyPair(ProtocolType.SHA256_WITH_RSA);
        String signature = PlatformUtils.sign(ProtocolType.SHA256_WITH_RSA, keyPair.getPrivate().getEncoded(), "data");

        // assert
        assertTrue(PlatformUtils.verifySignature(ProtocolType.SHA256_WITH_RSA, keyPair.getPublic().getEncoded(), "data", signature));
    }

    @Test
    void generateAndUseECDSAKeyPairShouldWork() {
        // prepare and act
        KeyPair keyPair = PlatformUtils.generateKeyPair(ProtocolType.SHA256_WITH_ECDSA);
        String signature = PlatformUtils.sign(ProtocolType.SHA256_WITH_ECDSA, keyPair.getPrivate().getEncoded(), "data");

        // assert
        assertTrue(PlatformUtils.verifySignature(ProtocolType.SHA256_WITH_ECDSA, keyPair.getPublic().getEncoded(), "data", signature));
    }

    private static final String REQUEST = "{\"_type\":\"CreateAssetRequest\",\"requestId\":\"61153e87-bf78-4444-b177-0d3e5969ef16\",\"assetId\":\"ada0e3a7-83b0-4cf3-8134-0b47f864e676\",\"assetTypeId\":\"a8eea81d-4b80-42da-aee6-226ea1d18420\",\"quantity\":\"1000000\"}";

    @Test
    void extractRequestIdShouldWork() {
        assertEquals("61153e87-bf78-4444-b177-0d3e5969ef16", PlatformUtils.getRequestId(REQUEST));
    }

    @Test
    void getTypeShouldWork() {
        assertEquals("CreateAssetRequest", PlatformUtils.getType(REQUEST));
    }

    @Test
    void mutateRequestIdShouldWork() {
        String body = PlatformUtils.mutateRequestId(REQUEST, "testRequestId");
        assertEquals("{\"_type\":\"CreateAssetRequest\",\"requestId\":\"testRequestId\",\"assetId\":\"ada0e3a7-83b0-4cf3-8134-0b47f864e676\",\"assetTypeId\":\"a8eea81d-4b80-42da-aee6-226ea1d18420\",\"quantity\":\"1000000\"}",
                body);
    }

}
