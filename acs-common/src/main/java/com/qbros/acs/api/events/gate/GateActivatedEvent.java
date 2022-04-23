package com.qbros.acs.api.events.gate;


import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GateActivatedEvent extends AbsEvent<GateAggregateID> {

    public GateActivatedEvent(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
