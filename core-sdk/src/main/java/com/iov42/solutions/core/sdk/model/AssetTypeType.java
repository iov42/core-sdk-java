package com.iov42.solutions.core.sdk.model;

/**
 * Represents the type of an AssetType.
 */
public enum AssetTypeType {

    /**
     * A unique Asset.
     */
    UNIQUE("Unique"),

    /**
     * A quantifiable Asset.
     */
    QUANTIFIABLE("Quantifiable");

    private final String type;

    AssetTypeType(String type) {
        this.type = type;
    }

    /**
     * Returns the {@code String} representation of the type.
     *
     * @return the {@code String} representation of the type
     */
    public String getType() {
        return type;
    }
}
