package com.iov42.solutions.core.sdk.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Structure for Claims.
 */
public class Claims {

    private final Collection<String> plainClaims;

    /**
     * Initializes a new Claims instance.
     *
     * @param plainClaims collection of plain claims
     */
    public Claims(Collection<String> plainClaims) {
        this.plainClaims = plainClaims;
    }

    /**
     * Creates a new {@link Claims} instance.
     *
     * @param plainClaims an array of plain claims
     * @return a new {@link Claims} instance
     */
    public static Claims of(String... plainClaims) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, plainClaims);
        return new Claims(list);
    }

    /**
     * Returns a collection of plain claims.
     *
     * @return a collection of plain claims
     */
    public Collection<String> getPlainClaims() {
        return plainClaims;
    }
}
