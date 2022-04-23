package com.qbros.acs.query.models.mappers;

import com.qbros.acs.api.events.personnel.PersonnelAddedEvent;
import com.qbros.acs.api.responses.personnel.PersonnelDetails;
import com.qbros.acs.api.responses.personnel.PersonnelNameAndId;
import com.qbros.acs.api.sharedmodels.domain.E_mail;
import com.qbros.acs.api.sharedmodels.domain.Name;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import com.qbros.acs.query.models.PersonnelEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonnelModelMapper {

    public PersonnelDetails toModel(PersonnelEntity entity) {
        return new PersonnelDetails(new PersonnelID(entity.getPersonnelID()),
                new Name(entity.getFirstName()),
                new Name(entity.getLastName()),
                new E_mail(entity.getEmail()));
    }

    public PersonnelNameAndId toSimpleModel(PersonnelEntity entity) {
        return new PersonnelNameAndId(entity.getPersonnelID(),
                String.format("%S %S (%S)", entity.getFirstName(), entity.getLastName(), entity.getPersonnelID()));
    }

    public PersonnelEntity eventToEntity(PersonnelAddedEvent event) {
        return new PersonnelEntity(event.getAggregateId().getPersonnelID().stringValue(),
                event.getFirstName().getValue(),
                event.getLastName().getValue(),
                event.getEmail().getValue());
    }
}