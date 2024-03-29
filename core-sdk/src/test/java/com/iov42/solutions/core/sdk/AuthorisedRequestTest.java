package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.model.SignatoryInfo;
import com.iov42.solutions.core.sdk.model.requests.command.AuthorisedRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateAssetTypeRequest;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorisedRequestTest {

    @Test
    void authorisedRequestShouldBeSerializedAndDeserialized() {
        // prepare
        SignatoryInfo signatoryInfo =
                new SignatoryInfo("5678", ProtocolType.SHA256_WITH_RSA, PlatformUtils.generateKeyPair(ProtocolType.SHA256_WITH_RSA));

        CreateAssetTypeRequest request = CreateAssetTypeRequest.unique("1234");
        AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request).authorise(signatoryInfo);

        // act
        String deserialized = authorisedRequest.serialize();
        AuthorisedRequest deserializedRequest = AuthorisedRequest.from(deserialized);

        // assert
        assertEquals(authorisedRequest.getRequestId(), deserializedRequest.getRequestId());
        assertEquals(authorisedRequest.getBody(), deserializedRequest.getBody());
        assertEquals(authorisedRequest.isRequireClaimHeaderCheck(), deserializedRequest.isRequireClaimHeaderCheck());
        assertEquals(authorisedRequest.getHeaders().size(), deserializedRequest.getHeaders().size());
        assertEquals(authorisedRequest.getAuthorisations().size(), deserializedRequest.getAuthorisations().size());
        assertEquals(
                authorisedRequest.getAuthorisations().get(0).getIdentityId(),
                deserializedRequest.getAuthorisations().get(0).getIdentityId());
        assertEquals(
                authorisedRequest.getAuthorisations().get(0).getDelegateIdentityId(),
                deserializedRequest.getAuthorisations().get(0).getDelegateIdentityId());
        assertEquals(
                authorisedRequest.getAuthorisations().get(0).getSignature(),
                deserializedRequest.getAuthorisations().get(0).getSignature());
        assertEquals(
                authorisedRequest.getAuthorisations().get(0).getProtocolId(),
                deserializedRequest.getAuthorisations().get(0).getProtocolId());
    }
}
