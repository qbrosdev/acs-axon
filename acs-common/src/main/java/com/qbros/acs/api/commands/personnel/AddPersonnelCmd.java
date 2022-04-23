package com.qbros.acs.api.commands.personnel;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.sharedmodels.domain.E_mail;
import com.qbros.acs.api.sharedmodels.domain.Name;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class AddPersonnelCmd extends AbsCommand<PersonnelAggregateID> {

    Name firstName;
    Name lastName;
    E_mail email;

    public AddPersonnelCmd(PersonnelAggregateID aggregateId, Name firstName, Name lastName, E_mail email) {
        super(aggregateId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
