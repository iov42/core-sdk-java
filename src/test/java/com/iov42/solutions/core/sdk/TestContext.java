package com.iov42.solutions.core.sdk;

import com.iov42.solutions.core.sdk.model.IovKeyPair;

public class TestContext {

    private String assetId;

    private String assetTypeId;

    private String assetTypeQuantifiableId;

    private String assetWithQuantityId;

    private boolean createdIdentity = false;

    private String identityId;

    private IovKeyPair keyPair;

    private String requestId;

    private String subjectAssetTypeId;

    private String subjectId;

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {

        this.assetTypeId = assetTypeId;
    }

    public String getAssetTypeQuantifiableId() {
        return assetTypeQuantifiableId;
    }

    public void setAssetTypeQuantifiableId(String assetTypeQuantifiableId) {
        this.assetTypeQuantifiableId = assetTypeQuantifiableId;
    }

    public String getAssetWithQuantityId() {
        return assetWithQuantityId;
    }

    public void setAssetWithQuantityId(String assetWithQuantityId) {
        this.assetWithQuantityId = assetWithQuantityId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public IovKeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(IovKeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSubjectAssetTypeId() {
        return subjectAssetTypeId;
    }

    public void setSubjectAssetTypeId(String subjectAssetTypeId) {
        this.subjectAssetTypeId = subjectAssetTypeId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isCreatedIdentity() {
        return createdIdentity;
    }

    public void setCreatedIdentity(boolean createdIdentity) {
        this.createdIdentity = createdIdentity;
    }
}
