package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import com.iov42.solutions.core.sdk.utils.serialization.JsonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilsTest {

    @Test
    void requestShouldBeDeserialized() {
        String s = "{\"requestId\":\"request-123\",\"resources\":[\"/api/v1/identities/identity-id\"],\"proof\":\"/api/v1/proofs/033cd061-6c4f-41c4-85bd-b9630d59e658\"}";
        RequestInfoResponse request = JsonUtils.fromJson(s, RequestInfoResponse.class);

        assertEquals("request-123", request.getRequestId());
        assertEquals(1, request.getResources().size());
        assertEquals("/api/v1/identities/identity-id", request.getResources().get(0));
        assertEquals("/api/v1/proofs/033cd061-6c4f-41c4-85bd-b9630d59e658", request.getProof());
    }

    @Test
    void requestShouldBeDeserializedAndAdditionalPropertiesIgnored() {
        String s = "{\"aNumber\": 4711, \"requestId\":\"request-123\",\"resources\":[\"/api/v1/identities/identity-id\"],\"proof\":\"/api/v1/proofs/033cd061-6c4f-41c4-85bd-b9630d59e658\"}";
        RequestInfoResponse request = JsonUtils.fromJson(s, RequestInfoResponse.class);

        assertEquals("request-123", request.getRequestId());
        assertEquals(1, request.getResources().size());
        assertEquals("/api/v1/identities/identity-id", request.getResources().get(0));
        assertEquals("/api/v1/proofs/033cd061-6c4f-41c4-85bd-b9630d59e658", request.getProof());
    }

    @Test
    void protocolEnumShouldBeConvertedCorrectlyToJson() {
        String s = JsonUtils.toJson(ProtocolType.SHA256_WITH_ECDSA);
        assertEquals("\"SHA256WithECDSA\"", s);
    }
}
