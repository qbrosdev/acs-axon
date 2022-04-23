package com.qbros.acs.api.sharedmodels.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = false)
@Value
@AllArgsConstructor
public class ActionResult extends ValueObject {

    Action action;
    boolean success;
    String info;

    public ActionResult(Action action, boolean success) {
        this.action = action;
        this.success = success;
        this.info = "";
    }

    @Override
    public String stringValue() {
        return String.format("%S-%s-%s", action, success, info);
    }
}
