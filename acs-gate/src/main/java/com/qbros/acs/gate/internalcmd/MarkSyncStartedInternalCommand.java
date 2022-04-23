package com.qbros.acs.gate.internalcmd;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class MarkSyncStartedInternalCommand extends AbsCommand<GateAggregateID> {

    public MarkSyncStartedInternalCommand(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
