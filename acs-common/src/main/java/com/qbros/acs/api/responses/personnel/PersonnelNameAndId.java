package com.qbros.acs.api.responses.personnel;

import com.qbros.acs.api.responses.QueryResp;
import lombok.Value;

@Value
public class PersonnelNameAndId implements QueryResp {

    String personnelId;
    String fullName;
}
