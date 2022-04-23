package com.qbros.acs.api.sharedmodels.domain;

public enum Action {

    EXIT, ENTER;

    public boolean isEnter() {
        return this == ENTER;
    }

    public boolean isExit() {
        return this == EXIT;
    }
}
