package com.qbros.acs.api.commands.gate;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
public class ModifyGateCmd extends AbsCommand<GateAggregateID> {

    String gateName;
    GateType gateType;
    List<String> allowedPersonnel;

    public ModifyGateCmd(GateAggregateID aggregateId, String gateName, GateType gateType, List<String> allowedPersonnel) {
        super(aggregateId);
        this.gateName = gateName;
        this.gateType = gateType;
        this.allowedPersonnel = allowedPersonnel;
    }
}
