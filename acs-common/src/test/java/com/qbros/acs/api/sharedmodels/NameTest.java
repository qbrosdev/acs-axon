package com.qbros.acs.api.sharedmodels;

import com.qbros.acs.api.sharedmodels.domain.Name;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"abc", "abc afdf", "ab", "a'dd", "dfdf"})
    void initNoExp(String input) {
        assertDoesNotThrow(() -> new Name(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12231", "abc 1", "1 abc", "12 dfdf 2323", " abc"})
    void initNameTest(String input) {
        assertThrows(ConstraintViolationException.class, () -> new Name(input));
    }
}