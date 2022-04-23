package com.qbros.acs.api.responses.gate;

import com.qbros.acs.api.responses.QueryResp;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import lombok.Value;

@Value
public class GateSummary implements QueryResp {

    GateID gate_id;
    String name;
    GateType gateType;
    boolean active;
    SyncStatus syncStatus;
}
