package com.qbros.acs.api.sharedmodels.domain;

import lombok.Value;

@Value
public class GateAggregateID implements AggregateID {

    GateID gateID;

    public GateAggregateID(GateID gateID) {
        this.gateID = gateID;
    }

    public GateAggregateID(String gateIDVal) {
        this.gateID = new GateID(gateIDVal);
    }
}
