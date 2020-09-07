package com.iov42.solutions.core.sdk.demo;

import com.iov42.solutions.core.sdk.BaseIntegrationTest;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.command.*;
import com.iov42.solutions.core.sdk.model.requests.get.GetAssetClaimRequest;
import com.iov42.solutions.core.sdk.model.requests.get.GetAssetRequest;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The class holds an integration test in form of a simple (artificial) scenario.
 * <p>
 * In this scenario Alice wants to sell her car to Bob. Bob only wants to buy the car if Alice can prove that the
 * first registration of the car was not longer than ten years ago (assuming the reference year is 2020).
 * <p>
 * Also in this scenario we assume that there is a fictional authority responsible for motor vehicles
 * (MVA - like the DMV in the US or the DVLA in Britain, ...). The MVA identity has to create an Asset Type "Car"
 * as preparation step.
 *
 */
public class DemoTest extends BaseIntegrationTest {

    /**
     * Helper function to create identities.
     *
     * @param identityId the Identity identifier
     * @return a {@link SignatoryInfo} of the created Identity
     */
    private SignatoryInfo createDemoIdentity(String identityId) {
        SignatoryInfo signatoryInfo = new SignatoryInfo(identityId, ProtocolType.SHA256WithRSA, PlatformUtils.generateKeyPair(ProtocolType.SHA256WithRSA));
        String publicKey = PlatformUtils.encodeBase64(signatoryInfo.getPublicKey());
        PublicCredentials credentials = new PublicCredentials(ProtocolType.SHA256WithRSA, publicKey);

        CreateIdentityRequest request = new CreateIdentityRequest(identityId, credentials);
        client().send(request, signatoryInfo)
                .whenComplete(BaseIntegrationTest::assertResponseSuccess).join();

        return signatoryInfo;
    }

    /**
     * The test scenario as outlined in the class documentation.
     *
     */
    @Test
    public void demoScenarioShouldRunThrough() {

        // 0. create an Identity for Motor Vehicle Authority (MVA)
        // MVA is a imaginary state authority for motor vehicles
        var mva = createDemoIdentity(randomId());

        // 1. MVA creates an AssetType to represent a Car
        var carAssetTypeId = randomId();
        client().send(CreateAssetTypeRequest.Unique(carAssetTypeId), mva).join();

        // 2. create an Identity for Alice (an individual)
        var alice = createDemoIdentity(randomId());

        // 3. create an Identity for Bob (an individual)
        var bob = createDemoIdentity(randomId());

        // 4. Alice creates an instance of a car AssetType
        var aliceCarAssetId = randomId();
        client().send(new CreateAssetRequest(carAssetTypeId, aliceCarAssetId), alice)
                .join();

        // 5. Alice claims the first registration of the car happened in 2010
        var registryDateClaimPlainText = "first-registration:10/02/2010";
        var registryDateClaim = Claims.of(registryDateClaimPlainText);
        client().send(new CreateAssetClaimsRequest(carAssetTypeId, aliceCarAssetId, registryDateClaim), alice)
                .join();

        // 6. MVA endorses Alice's claim of the registration date (as it's the authority anyone can trust that endorsement)
        var registryDateEndorsement = new Endorsements(mva, carAssetTypeId, aliceCarAssetId, registryDateClaim);
        client().send(
                new CreateAssetEndorsementsRequest(carAssetTypeId, aliceCarAssetId, mva.getIdentityId(), registryDateEndorsement),
                mva).join();

        ensureEventualConsistency();

        // 7. Bob want's to buy the car and checks that the registration claim from Alice is in fact endorsed by the MVA
        var claimResponse = client().queryAs(bob)
                .getAssetClaim(new GetAssetClaimRequest(carAssetTypeId, aliceCarAssetId, PlatformUtils.hashClaim(registryDateClaimPlainText)));
        assertEquals(1, claimResponse.getEndorsements().size(), "There should be exactly one endorsement");
        assertEquals(mva.getIdentityId(), claimResponse.getEndorsements().get(0).getEndorserId(), "The endorser should be the MVA identity");
        assertTrue(
                PlatformUtils.verifyEndorsement(
                        claimResponse.getEndorsements().get(0).getEndorsement(),
                        PlatformUtils.hashClaim(registryDateClaimPlainText),
                        carAssetTypeId,
                        aliceCarAssetId,
                        mva.toPublicCredentials()),
                "The endorsement should be verified with the public credentials of the MVA identity"
        );

        // 8. (off-chain) Bob is happy with the car and trusts the registration year now - he pays Alice the requested amount of money

        // 9. Alice in turn transfers the car instance to Bob
        client().send(
                TransfersRequest.of(new TransferOwnership(carAssetTypeId, aliceCarAssetId, alice.getIdentityId(), bob.getIdentityId())),
                alice)
                .join();

        ensureEventualConsistency();

        // 10. The end - at this point Bob is the Owner of the Unique Asset (Alice's Car)
        var assetResponse = client().queryAs(bob).getAsset(new GetAssetRequest(carAssetTypeId, aliceCarAssetId));
        assertEquals(bob.getIdentityId(), assetResponse.getOwnerId(), "Bob should now be the owner of Alice's car");
    }
}
