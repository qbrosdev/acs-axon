package com.qbros.acs.api.sharedmodels;

import com.qbros.acs.api.sharedmodels.domain.E_mail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"abc@df.com", "abc@df.co.uk", "df.abc@df.co.uk", "dfabc@dfcouk"})
    void initNoExp(String input) {
        assertDoesNotThrow(() -> new E_mail(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab@c@df.com", "abcdf.co.uk"})
    void initNameTest(String input) {
        assertThrows(ConstraintViolationException.class, () -> new E_mail(input));
    }
}