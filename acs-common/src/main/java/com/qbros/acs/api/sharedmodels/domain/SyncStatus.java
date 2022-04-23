package com.qbros.acs.api.sharedmodels.domain;

public enum SyncStatus {

    SYNC, NO_SYNC, SYNCING;

    public boolean isSYNC() {
        return this == SYNC;
    }

    public boolean isNO_SYNC() {
        return this == NO_SYNC;
    }

    public boolean isSYNCING() {
        return this == SYNCING;
    }
}
