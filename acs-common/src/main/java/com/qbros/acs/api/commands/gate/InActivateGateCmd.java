package com.qbros.acs.api.commands.gate;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class InActivateGateCmd extends AbsCommand<GateAggregateID> {

    public InActivateGateCmd(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
