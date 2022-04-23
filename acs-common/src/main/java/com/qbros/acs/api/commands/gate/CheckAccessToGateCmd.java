package com.qbros.acs.api.commands.gate;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.Action;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class CheckAccessToGateCmd extends AbsCommand<GateAggregateID> {

    PersonnelID personnelID;
    boolean personnelPresent;
    Action action;

    public CheckAccessToGateCmd(GateAggregateID aggregateId, PersonnelID personnelID, boolean isPresent, Action action) {
        super(aggregateId);
        this.personnelID = personnelID;
        this.personnelPresent = isPresent;
        this.action = action;
    }
}
