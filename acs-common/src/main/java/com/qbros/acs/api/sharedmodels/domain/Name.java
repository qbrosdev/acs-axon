package com.qbros.acs.api.sharedmodels.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = false)
@Value
public class Name extends ValueObject {

    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Name must start with Letters and contains at least 2 (Alphanumeric,space or [',. -])characters")
    String value;

    public Name(String value) {
        this.value = value;
        validate(this);
    }

    @Override
    public String stringValue() {
        return value;
    }
}
