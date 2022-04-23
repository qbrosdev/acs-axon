package com.qbros.acs.api.events.gate;


import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GateInActivatedEvent extends AbsEvent<GateAggregateID> {

    public GateInActivatedEvent(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
