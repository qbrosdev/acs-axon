package com.qbros.acs.api.commands.personnel;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class DeletePersonnelCmd extends AbsCommand<PersonnelAggregateID> {

    public DeletePersonnelCmd(PersonnelAggregateID aggregateId) {
        super(aggregateId);
    }
}
