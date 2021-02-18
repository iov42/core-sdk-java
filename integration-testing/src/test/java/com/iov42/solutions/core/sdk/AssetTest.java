package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.Claims;
import com.iov42.solutions.core.sdk.model.Endorsements;
import com.iov42.solutions.core.sdk.model.requests.command.*;
import com.iov42.solutions.core.sdk.model.requests.get.GetAssetClaimsRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetAssetTransactionsRequest;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AssetTest extends BaseIntegrationTest {

    @Test
    public void uniqueAssetShouldBeCreated() {

        // prepare
        var actor = createIdentity(randomId());

        var assetTypeId = randomId();
        createUniqueAssetType(actor, assetTypeId);

        // act
        var assetId = randomId();

        var createAssetRequest = new CreateAssetRequest(assetTypeId, assetId);
        var response = client().send(createAssetRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/assets/" + assetId);
    }

    @Test
    public void quantifiableAssetShouldBeCreated() {

        // prepare
        var actor = createIdentity(randomId());

        var assetTypeId = randomId();
        createQuantifiableAssetType(actor, assetTypeId, 2);

        // act
        var assetId = randomId();

        var createAssetRequest = new CreateAssetRequest(assetTypeId, assetId, BigInteger.valueOf(100));
        var response = client().send(createAssetRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/assets/" + assetId);
    }

    @Test
    public void claimsOnUniqueAssetShouldBeCreated() {
        // prepare
        var actor = createIdentity(randomId());

        var assetTypeId = randomId();
        createUniqueAssetType(actor, assetTypeId);

        var assetId = randomId();
        createUniqueAsset(actor, assetTypeId, assetId);

        // act
        var claims = Claims.of("claim1");
        var createAssetClaimsRequest = new CreateAssetClaimsRequest(assetTypeId, assetId, claims);
        var response = client().send(createAssetClaimsRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        var hashedClaim = PlatformUtils.hashClaim("claim1");
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/assets/" + assetId + "/claims/" + hashedClaim);

        // query
        ensureEventualConsistency();
        var claimsResponse = client().queryAs(actor).getAssetClaims(new GetAssetClaimsRequest(assetTypeId, assetId));
        assertNotNull(claimsResponse);
        assertEquals(1, claimsResponse.getClaims().size());
        assertEquals(hashedClaim, claimsResponse.getClaims().get(0).getClaim());
    }

    @Test
    public void claimsOnUniqueAssetShouldBeEndorsed() {

        // prepare
        var actor = createIdentity(randomId());
        var endorserInfo = createIdentity(randomId());

        var assetTypeId = randomId();
        createUniqueAssetType(actor, assetTypeId);

        var assetId = randomId();
        createUniqueAsset(actor, assetTypeId, assetId);

        var claims = Claims.of("claim1");
        createAssetClaims(actor, assetTypeId, assetId, claims);

        // act
        var endorsements = new Endorsements(endorserInfo, assetTypeId, assetId, claims);
        var createAssetEndorsementsRequest = new CreateAssetEndorsementsRequest(assetTypeId, assetId, endorserInfo.getIdentityId(), endorsements);
        var response = client().send(createAssetEndorsementsRequest, endorserInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        var hashedClaim = PlatformUtils.hashClaim("claim1");
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/assets/" + assetId + "/claims/" + hashedClaim);
    }

    @Test
    public void claimsOnAccountShouldBeCreated() {
        // prepare
        var actor = createIdentity(randomId());

        var assetTypeId = randomId();
        createQuantifiableAssetType(actor, assetTypeId, 2);

        var assetId = randomId();
        createQuantifiableAsset(actor, assetTypeId, assetId, 10);

        // act
        var claims = Claims.of("claim1");
        var createAssetClaimsRequest = new CreateAssetClaimsRequest(assetTypeId, assetId, claims);
        var response = client().send(createAssetClaimsRequest, actor)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        var hashedClaim = PlatformUtils.hashClaim("claim1");
        assertResources(response.getResources(), "/asset-types/" + assetTypeId + "/assets/" + assetId + "/claims/" + hashedClaim);
    }

    @Test
    public void quantityOfAssetShouldBeTransferred() {

        // prepare
        var sender = createIdentity(randomId());
        var receiver = createIdentity(randomId());

        var assetTypeId = randomId();
        createQuantifiableAssetType(sender, assetTypeId, 2);

        var senderAccountId = randomId();
        createQuantifiableAsset(sender, assetTypeId, senderAccountId, 100);

        var receiverAccountId = randomId();
        createQuantifiableAsset(receiver, assetTypeId, receiverAccountId, 0);

        // act
        var transferRequest =
                TransfersRequest.of(new TransferQuantity(assetTypeId, senderAccountId, receiverAccountId, 50));
        var response = client().send(transferRequest, sender)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(),
                "/asset-types/" + assetTypeId + "/assets/" + senderAccountId,
                "/asset-types/" + assetTypeId + "/assets/" + receiverAccountId
        );
    }

    @Test
    public void shouldGetQuantifiableAssetTransferTransaction() {
        // prepare
        var sender = createIdentity(randomId());
        var receiver = createIdentity(randomId());

        var assetTypeId = randomId();
        createQuantifiableAssetType(sender, assetTypeId, 2);

        var senderAccountId = randomId();
        createQuantifiableAsset(sender, assetTypeId, senderAccountId, 100);

        var receiverAccountId = randomId();
        createQuantifiableAsset(receiver, assetTypeId, receiverAccountId, 0);

        transferQuantity(assetTypeId, senderAccountId, sender, receiverAccountId);

        // act
        var get = new GetAssetTransactionsRequest(assetTypeId, senderAccountId);
        var response = client().queryAs(sender).getAssetTransactions(get);

        // assert
        assertNotNull(response);
        assertEquals(1, response.getTransactions().size());
        assertEquals(senderAccountId, response.getTransactions().get(0).getSender().getAssetId());
        assertEquals(receiverAccountId, response.getTransactions().get(0).getRecipient().getAssetId());
    }

    @Test
    public void accountShouldBeTransferred() {

        // prepare
        var sender = createIdentity(randomId());
        var receiver = createIdentity(randomId());

        var assetTypeId = randomId();
        createQuantifiableAssetType(sender, assetTypeId, 2);

        var senderAccountId = randomId();
        createQuantifiableAsset(sender, assetTypeId, senderAccountId, 100);

        // act
        var transferRequest =
                TransfersRequest.of(new TransferOwnership(assetTypeId, senderAccountId, sender.getIdentityId(), receiver.getIdentityId()));
        var response = client().send(transferRequest, sender)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(),
                "/asset-types/" + assetTypeId + "/assets/" + senderAccountId
        );
    }

    @Test
    public void uniqueAssetShouldBeTransferred() {

        // prepare
        var sender = createIdentity(randomId());
        var receiver = createIdentity(randomId());

        var assetTypeId = randomId();
        createUniqueAssetType(sender, assetTypeId);

        var assetId = randomId();
        createUniqueAsset(sender, assetTypeId, assetId);

        // act
        var transferRequest =
                TransfersRequest.of(new TransferOwnership(assetTypeId, assetId, sender.getIdentityId(), receiver.getIdentityId()));
        var response = client().send(transferRequest, sender)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess)
                .join();

        // assert
        assertResources(response.getResources(),
                "/asset-types/" + assetTypeId + "/assets/" + assetId
        );
    }
}
