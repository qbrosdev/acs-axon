package com.qbros.acs.api.sharedmodels.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;

@EqualsAndHashCode(callSuper = false)
@Value
public class E_mail extends ValueObject {

    @Email
    String value;

    public E_mail(String value) {
        this.value = value;
        validate(this);
    }

    @Override
    public String stringValue() {
        return value;
    }
}
