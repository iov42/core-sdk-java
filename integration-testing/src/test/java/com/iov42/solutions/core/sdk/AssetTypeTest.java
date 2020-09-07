package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.Claims;
import com.iov42.solutions.core.sdk.model.Endorsements;
import com.iov42.solutions.core.sdk.model.requests.command.AuthorisedRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateAssetTypeClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateAssetTypeEndorsementsRequest;
import com.iov42.solutions.core.sdk.model.requests.command.CreateAssetTypeRequest;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssetTypeTest extends BaseIntegrationTest {

    @Test
    public void uniqueAssetTypeShouldBeCreated() {

        // prepare
        var actor = createIdentity(randomId());
        var assetTypeId = randomId();

        // act
        var createAssetTypeRequest = CreateAssetTypeRequest.Unique(assetTypeId);

        var response = client().send(createAssetTypeRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        // assert
        assertNotNull(response);
        assertTrue(response.hasFinished());
    }

    @Test
    public void claimsForAssetTypeShouldBeCreated() {

        // prepare
        var actor = createIdentity(randomId());
        var assetTypeId = randomId();
        createUniqueAssetType(actor, assetTypeId);

        // act
        var claims = Claims.of("claim1", "claim2");
        var createAssetTypeClaimsRequest = new CreateAssetTypeClaimsRequest(assetTypeId, claims);

        var response = client().send(createAssetTypeClaimsRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        // assert
        var hashedClaim1 = PlatformUtils.hashClaim("claim1");
        var hashedClaim2 = PlatformUtils.hashClaim("claim2");
        assertResources(response.getResources(),
                "/asset-types/" + assetTypeId + "/claims/" + hashedClaim1,
                "/asset-types/" + assetTypeId + "/claims/" + hashedClaim2);
    }

    @Test
    public void claimsForAssetTypeShouldBeCreatedAndEndorsed() {

        // prepare
        var actor = createIdentity(randomId());
        var endorserInfo = createIdentity(randomId());

        var assetTypeId = randomId();
        createUniqueAssetType(actor, assetTypeId);

        // act
        var claims = Claims.of("claim1");
        var endorsements = new Endorsements(endorserInfo, assetTypeId, claims);
        var createAssetTypeEndorsementsRequest =
                new CreateAssetTypeEndorsementsRequest(assetTypeId, endorserInfo.getIdentityId(), endorsements);

        var authorisedRequest = AuthorisedRequest.from(createAssetTypeEndorsementsRequest)
                .authorise(actor).authorise(endorserInfo);

        var response = client().send(authorisedRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        // assert
        var hashedClaim = PlatformUtils.hashClaim("claim1");
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/claims/" + hashedClaim);
        // TODO check endorsement resource here
    }

    @Test
    public void claimsForAssetTypeShouldBeEndorsed() {

        // prepare
        var actor = createIdentity(randomId());
        var endorserInfo = createIdentity(randomId());

        var assetTypeId = randomId();
        createUniqueAssetType(actor, assetTypeId);

        var claims = Claims.of("claim1");
        createAssetTypeClaims(actor, assetTypeId, claims);

        // act
        var endorsements = new Endorsements(endorserInfo, assetTypeId, claims);
        var createAssetTypeEndorsementsRequest =
                new CreateAssetTypeEndorsementsRequest(assetTypeId, endorserInfo.getIdentityId(), endorsements);

        var response = client().send(createAssetTypeEndorsementsRequest, endorserInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        // assert
        var hashedClaim = PlatformUtils.hashClaim("claim1");
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/claims/" + hashedClaim);
        // TODO check endorsement resource here
    }

    @Test
    public void quantifiableAssetTypeShouldBeCreated() {

        // prepare
        var actor = createIdentity(randomId());
        var assetTypeId = randomId();

        // act
        var createAssetTypeRequest = CreateAssetTypeRequest.Quantifiable(assetTypeId, 2);
        var response = client().send(createAssetTypeRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        // assert
        assertNotNull(response);
        assertTrue(response.hasFinished());
    }
}
