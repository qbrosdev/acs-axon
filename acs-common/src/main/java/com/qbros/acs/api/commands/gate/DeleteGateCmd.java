package com.qbros.acs.api.commands.gate;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class DeleteGateCmd extends AbsCommand<GateAggregateID> {

    public DeleteGateCmd(GateAggregateID aggregateId) {
        super(aggregateId);
    }
}
