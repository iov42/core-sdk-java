package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.PublicCredentials;
import com.iov42.solutions.core.sdk.model.requests.get.*;
import com.iov42.solutions.core.sdk.model.responses.*;

/**
 * Interface for querying the iov42 platform.
 */
public interface PlatformQueryExecutor {

    /**
     * Gets information about an Identity.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/identities/paths/~1identities~1{identityId}/get">iov42 platform API specification: identities</a>
     *
     * @param request the {@link GetIdentityRequest}
     * @return a {@link GetIdentityResponse}
     */
    GetIdentityResponse getIdentity(GetIdentityRequest request);

    /**
     * Gets information about a public key of an Identity.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/identities/paths/~1identities~1{identityId}~1public-key/get">iov42 platform API specification: identities public key</a>
     *
     * @param request the {@link GetIdentityRequest}
     * @return {@link PublicCredentials}
     */
    PublicCredentials getIdentityPublicKey(GetIdentityRequest request);

    /**
     * Gets information about a claim of an Identity.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/identities/paths/~1identities~1{identityId}~1claims~1{hashedClaim}/get">iov42 platform API specification: identities claim</a>
     *
     * @param request the {@link GetIdentityClaimRequest}
     * @return a {@link ClaimEndorsementsResponse}
     */
    ClaimEndorsementsResponse getIdentityClaim(GetIdentityClaimRequest request);

    /**
     * Gets information about claims of an Identity.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/identities/paths/~1identities~1{identityId}~1claims/get">iov42 platform API specification: identities claims</a>
     *
     * @param request the {@link GetIdentityClaimsRequest}
     * @return a {@link GetClaimsResponse}
     */
    GetClaimsResponse getIdentityClaims(GetIdentityClaimsRequest request);

    /**
     * Gets information about an Asset.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/assets/paths/~1asset-types~1{assetTypeId}~1assets~1{assetId}/get">iov42 platform API specification: assets</a>
     *
     * @param request the {@link GetAssetRequest}
     * @return a {@link GetAssetResponse}
     */
    GetAssetResponse getAsset(GetAssetRequest request);

    /**
     * Gets information about an Asset claim.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/assets/paths/~1asset-types~1{assetTypeId}~1assets~1{assetId}~1claims~1{hashedClaim}/get">iov42 platform API specification: asset claim</a>
     *
     * @param request the {@link GetAssetClaimRequest}
     * @return a {@link ClaimEndorsementsResponse}
     */
    ClaimEndorsementsResponse getAssetClaim(GetAssetClaimRequest request);

    /**
     * Gets information about claims of an Asset.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/assets/paths/~1asset-types~1{assetTypeId}~1assets~1{assetId}~1claims/get">iov42 platform API specification: asset claims</a>
     *
     * @param request the {@link GetAssetClaimsRequest}
     * @return a {@link GetClaimsResponse}
     */
    GetClaimsResponse getAssetClaims(GetAssetClaimsRequest request);

    /**
     * Gets information of transactions of an Asset.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/transactions/paths/~1asset-types~1{assetTypeId}~1assets~1{assetId}~1transactions/get">iov42 platform API specification: transactions </a>
     *
     * @param request the {@link GetAssetTransactionsRequest}
     * @return a {@link GetAssetTransactionsResponse}
     */
    GetAssetTransactionsResponse getAssetTransactions(GetAssetTransactionsRequest request);

    /**
     * Gets information about an AssetType.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/assets/paths/~1asset-types~1{assetTypeId}/get">iov42 platform API specification: asset types</a>
     *
     * @param request the {@link GetAssetTypeRequest}
     * @return a {@link GetAssetTypeResponse}
     */
    GetAssetTypeResponse getAssetType(GetAssetTypeRequest request);

    /**
     * Gets information about claims of an AssetType.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/assets/paths/~1asset-types~1{assetTypeId}~1claims/get">iov42 platform API specification: asset type claims</a>
     *
     * @param request the {@link GetAssetTypeClaimsRequest}
     * @return a {@link GetClaimsResponse}
     */
    GetClaimsResponse getAssetTypeClaims(GetAssetTypeClaimsRequest request);

    /**
     * Gets information about an AssetType claim.
     *
     * @see <a href="https://tech.iov42.com/platform/api/#tag/assets/paths/~1asset-types~1{assetTypeId}~1claims~1{hashedClaim}/get">iov42 platform API specification: asset type claim</a>
     *
     * @param request the {@link GetAssetTypeClaimRequest}
     * @return a {@link GetClaimsResponse}
     */
    ClaimEndorsementsResponse getAssetTypeClaim(GetAssetTypeClaimRequest request);
}
