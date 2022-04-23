package com.qbros.acs.api.sharedmodels.domain;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Represents DDD value objects.
 */
public abstract class ValueObject {

    /**
     * The validity of values set for the value objects are checked using this validator object.
     */
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    /**
     * This method provides uniform way to represent the value of Value object
     *
     * @return String representation of the value object
     */
    public abstract String stringValue();
}
