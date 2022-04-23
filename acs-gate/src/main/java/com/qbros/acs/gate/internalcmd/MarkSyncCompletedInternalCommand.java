package com.qbros.acs.gate.internalcmd;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class MarkSyncCompletedInternalCommand extends AbsCommand<GateAggregateID> {

    public MarkSyncCompletedInternalCommand(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
