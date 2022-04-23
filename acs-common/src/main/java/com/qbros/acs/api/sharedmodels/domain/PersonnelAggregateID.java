package com.qbros.acs.api.sharedmodels.domain;

import lombok.Value;


@Value
public class PersonnelAggregateID implements AggregateID {

    PersonnelID personnelID;

    public PersonnelAggregateID(PersonnelID personnelID) {
        this.personnelID = personnelID;
    }

    public PersonnelAggregateID(String personnelID) {
        this.personnelID = new PersonnelID(personnelID);
    }

}
