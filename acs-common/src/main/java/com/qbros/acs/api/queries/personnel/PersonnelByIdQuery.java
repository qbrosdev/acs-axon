package com.qbros.acs.api.queries.personnel;

import com.qbros.acs.api.queries.AbsQuery;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class PersonnelByIdQuery extends AbsQuery {

    PersonnelID personnelId;
}
