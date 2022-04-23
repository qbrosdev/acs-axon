package com.qbros.acs.api.sharedmodels;

import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonnelIdTest {

    @ParameterizedTest
    @ValueSource(strings = {"123", "1234", "12345", "123456"})
    void initNoExp(String input) {
        assertDoesNotThrow(() -> new PersonnelID(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "abc 1", "1 abc", "12 dfdf 2323", " abc"})
    void initNameTest(String input) {
        assertThrows(ConstraintViolationException.class, () -> new PersonnelID(input));
    }
}