package com.iov42.solutions.core.sdk.model;

/**
 * Structure for Endorsements.
 */
public class Endorsements {

    private final SignatoryInfo endorserInfo;

    private final String subjectId;

    private final String subjectTypeId;

    private final Claims plainClaims;

    /**
     * Initialises a new endorsement object.
     *
     * @param endorserInfo the {@link SignatureInfo} of the endorser
     * @param subjectId    the Subject identifier (must never be {@code null})
     * @param claims       the {@link Claims} to be endorsed
     */
    public Endorsements(SignatoryInfo endorserInfo, String subjectId, Claims claims) {
        this(endorserInfo, null, subjectId, claims);
    }

    /**
     * Initialises a new endorsement object. This variant is for Asset endorsements.
     *
     * @param endorserInfo  the {@link SignatureInfo} of the endorser
     * @param subjectTypeId the SubjectType identifier (must not be {@code null} for Asset endorsements
     * @param subjectId     the Subject identifier (must never be {@code null})
     * @param claims        the {@link Claims} to be endorsed
     */
    public Endorsements(SignatoryInfo endorserInfo, String subjectTypeId, String subjectId, Claims claims) {
        this.endorserInfo = endorserInfo;
        this.subjectId = subjectId;
        this.subjectTypeId = subjectTypeId;
        this.plainClaims = claims;
    }

    /**
     * Returns the {@link SignatureInfo} of the endorser.
     *
     * @return the {@link SignatureInfo} of the endorser
     */
    public SignatoryInfo getEndorserInfo() {
        return endorserInfo;
    }

    /**
     * Returns the Subject identifier (represents either an Identity, an Asset, or an AssetType).
     *
     * @return the Subject identifier.
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * Returns the SubjectType identifier (only valid for Asset endorsements, holds the AssetType identifier in that case).
     *
     * @return the SubjectType identifier or {@code null} otherwise
     */
    public String getSubjectTypeId() {
        return subjectTypeId;
    }

    /**
     * Returns the {@link Claims} to be endorsed.
     *
     * @return the {@link Claims} to be endorsed
     */
    public Claims getClaims() {
        return plainClaims;
    }
}
