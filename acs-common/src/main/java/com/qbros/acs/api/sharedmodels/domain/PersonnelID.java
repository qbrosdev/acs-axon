package com.qbros.acs.api.sharedmodels.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = false)
@Value
@Getter(AccessLevel.PRIVATE)
public class PersonnelID extends ValueObject {

    @Pattern(regexp = "[\\d]{3,6}", message = "PersonnelID must be 3 to 6 digits")
    String value;

    public PersonnelID(String value) {
        this.value = value;
        validate(this);
    }

    @Override
    public String stringValue() {
        return value;
    }
}
