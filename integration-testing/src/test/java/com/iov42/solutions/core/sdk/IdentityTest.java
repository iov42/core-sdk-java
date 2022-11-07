package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.command.*;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetIdentityRequest;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentityTest extends BaseIntegrationTest {

    @Test
    void identityWithRSAShouldBeCreated() {

        // prepare
        var identityId = randomId();
        var signatoryInfo = generateSignatoryInfo(identityId, ProtocolType.SHA256_WITH_RSA);
        var publicKey = PlatformUtils.encodeBase64(signatoryInfo.getPublicKey());
        var credentials = new PublicCredentials(ProtocolType.SHA256_WITH_RSA, publicKey);

        // act
        var request = new CreateIdentityRequest(identityId, credentials);
        var response = client().send(request, signatoryInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(), "/identities/" + identityId);
    }

    @Test
    void identityWithECDSAShouldBeCreated() {

        // prepare
        var identityId = randomId();
        var signatoryInfo = generateSignatoryInfo(identityId, ProtocolType.SHA256_WITH_ECDSA);
        var publicKey = PlatformUtils.encodeBase64(signatoryInfo.getPublicKey());
        var credentials = new PublicCredentials(ProtocolType.SHA256_WITH_ECDSA, publicKey);

        // act
        var request = new CreateIdentityRequest(identityId, credentials);
        var response = client().send(request, signatoryInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(), "/identities/" + identityId);
    }

    @Test
    void publicKeyShouldBeRetrieved() {
        // prepare
        var identityId = randomId();
        var actor = createIdentity(identityId);
        ensureEventualConsistency();

        // act
        var publicCredentials = client().queryAs(actor).getIdentityPublicKey(new GetIdentityRequest(identityId));

        // assert
        var actorCredentials = actor.toPublicCredentials();
        assertEquals(actorCredentials.getKey(), publicCredentials.getKey());
        assertEquals(actorCredentials.getProtocolId(), publicCredentials.getProtocolId());
    }

    @Test
    void identityShouldBeRetrieved() {

        // prepare
        var identityId = randomId();
        var actor = createIdentity(identityId);
        ensureEventualConsistency();

        // act
        var result = client().queryAs(actor).getIdentity(new GetIdentityRequest(identityId));

        // assert
        assertNotNull(result);
        assertEquals(identityId, result.getIdentityId());
    }

    @Test
    void claimsOnIdentityShouldBeCreated() {

        // prepare
        var identityId = randomId();
        var actor = createIdentity(identityId);

        // act
        var createIdentityClaimsRequest = new CreateIdentityClaimsRequest(identityId, Claims.of("claim"));
        var response = client().send(createIdentityClaimsRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertEquals(1, response.getResources().size());
    }

    @Test
    void claimsOnIdentityShouldBeCreatedWhenAuthenticatedByOtherIdentity() {

        // prepare
        var otherId = randomId();
        var other = createIdentity(otherId);

        var identityId = randomId();
        var identity = createIdentity(identityId);

        // act
        var createIdentityClaimsRequest = new CreateIdentityClaimsRequest(identityId, Claims.of("claim"));
        var authorisedRequest = AuthorisedRequest.from(createIdentityClaimsRequest).authorise(identity);
        var response = client().send(authorisedRequest, other)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertEquals(1, response.getResources().size());
    }

    @Test
    void claimsOnIdentityShouldBeReadByOtherIdentity() {

        // prepare
        var otherId = randomId();
        var other = createIdentity(otherId);

        var identityId = randomId();
        var identity = createIdentity(identityId);

        var createIdentityClaimsRequest = new CreateIdentityClaimsRequest(identityId, Claims.of("claim"));
        client().send(createIdentityClaimsRequest, identity)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();
        ensureEventualConsistency();

        // act
        var response = client().queryAs(other).getIdentityClaims(new GetIdentityClaimsRequest(identityId));

        // assert
        assertEquals(1, response.getClaims().size());
        assertEquals(PlatformUtils.hashClaim("claim"), response.getClaims().get(0).getClaim());
    }

    @Test
    void claimOnIdentityShouldBeEndorsed() {

        // prepare
        var identityId = randomId();
        var identity = createIdentity(identityId);

        var endorserId = randomId();
        var endorser = createIdentity(endorserId);

        // act
        var claims = Claims.of("claim1");
        var createIdentityEndorsementsRequest =
                new CreateIdentityEndorsementsRequest(identityId, endorserId, new Endorsements(endorser, identityId, claims));

        var response = client()
                .send(AuthorisedRequest.from(createIdentityEndorsementsRequest).authorise(identity).authorise(endorser), identity)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertEquals(2, response.getResources().size());

        var hashedClaim = PlatformUtils.hashClaim("claim1");
        assertResources(response.getResources(),
                "identities/" + identityId + "/claims/" + hashedClaim
                // TODO at some point also assert the 2nd resource - the endorsement
        );
    }

    @Test
    void delegateShouldBeAdded() {
        // prepare

        var delegatorId = randomId();
        var delegator = createIdentity(delegatorId);

        var delegateId = randomId();
        var delegate = createIdentity(delegateId);

        // act
        var addDelegateRequest = new AddDelegateRequest(delegateId, delegatorId);
        var response = client()
                .send(AuthorisedRequest.from(addDelegateRequest).authorise(delegate).authorise(delegator), delegate)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertTrue(response.hasFinished());
        assertResources(response.getResources(), "identities/" + delegatorId);
    }
}
