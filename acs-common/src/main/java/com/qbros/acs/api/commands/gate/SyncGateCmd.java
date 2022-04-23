package com.qbros.acs.api.commands.gate;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class SyncGateCmd extends AbsCommand<GateAggregateID> {

    public SyncGateCmd(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
