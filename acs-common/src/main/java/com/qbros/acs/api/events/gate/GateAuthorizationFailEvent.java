package com.qbros.acs.api.events.gate;


import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.sharedmodels.domain.ActionResult;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GateAuthorizationFailEvent extends AbsEvent<GateAggregateID> {

    PersonnelID personnelID;
    ActionResult result;

    public GateAuthorizationFailEvent(GateAggregateID aggregateId, PersonnelID personnelID, ActionResult result) {
        super(aggregateId);
        this.personnelID = personnelID;
        this.result = result;
    }
}
