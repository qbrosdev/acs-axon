package com.qbros.acs.api.events.gate;


import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
public class GateCreatedEvent extends AbsEvent<GateAggregateID> {

    String gateName;
    GateType gateType;
    List<String> allowedPersonnel;

    public GateCreatedEvent(GateAggregateID aggregateId, String gateName, GateType gateType, List<String> allowedPersonnel) {
        super(aggregateId);
        this.gateName = gateName;
        this.gateType = gateType;
        this.allowedPersonnel = allowedPersonnel;
    }
}
