package com.qbros.acs.api.responses.personnel;

import com.qbros.acs.api.responses.QueryResp;
import com.qbros.acs.api.sharedmodels.domain.E_mail;
import com.qbros.acs.api.sharedmodels.domain.Name;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import lombok.Value;

@Value
public class PersonnelDetails implements QueryResp {

    PersonnelID personnelId;
    Name firstName;
    Name lastName;
    E_mail email;

    public PersonnelDetails(PersonnelID personnelId, Name firstName, Name lastName, E_mail email) {
        this.personnelId = personnelId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


}
