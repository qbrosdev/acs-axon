package com.qbros.acs.api.events.personnel;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.sharedmodels.domain.E_mail;
import com.qbros.acs.api.sharedmodels.domain.Name;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Value
public class PersonnelAddedEvent extends AbsEvent<PersonnelAggregateID> {

    Name firstName;
    Name lastName;
    E_mail email;

    public PersonnelAddedEvent(PersonnelAggregateID aggregateId, Name firstName, Name lastName, E_mail email) {
        super(aggregateId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
