package com.qbros.acs.api.responses.gate;

import com.qbros.acs.api.responses.QueryResp;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import lombok.Value;

import java.util.List;

@Value
public class GateDetails implements QueryResp {

    GateID gateId;
    String gateName;
    GateType gateType;
    SyncStatus syncStatus;
    List<String> allowedPersonnelIDs;
}
