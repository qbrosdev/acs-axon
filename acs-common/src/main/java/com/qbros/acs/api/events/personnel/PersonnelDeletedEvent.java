package com.qbros.acs.api.events.personnel;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Value
public class PersonnelDeletedEvent extends AbsEvent<PersonnelAggregateID> {

    public PersonnelDeletedEvent(PersonnelAggregateID aggregateId) {
        super(aggregateId);
    }
}
