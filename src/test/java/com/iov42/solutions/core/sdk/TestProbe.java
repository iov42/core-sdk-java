package com.iov42.solutions.core.sdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProbe {

    @ParameterizedTest(name = "csv compare")
    @CsvSource({
            "0,1,1",
            "1,2,3",
            "49,51,100",
            "1,100,101"
    })
    public void testComparison(int first, int second, int result) {
        assertEquals(result, first + second);
    }

    @Test
    @DisplayName("Simple calculation")
    public void testDivision() {
        int result = 10 / 10;
        assertEquals(1, result);
    }

}
