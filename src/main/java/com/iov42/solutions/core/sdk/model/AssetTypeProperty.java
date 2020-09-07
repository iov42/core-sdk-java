package com.iov42.solutions.core.sdk.model;

public enum AssetTypeProperty {

    UNIQUE("Unique"),
    QUANTIFIABLE("Quantifiable");

    private final String type;

    AssetTypeProperty(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
