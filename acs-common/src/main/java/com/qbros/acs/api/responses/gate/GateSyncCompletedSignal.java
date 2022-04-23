package com.qbros.acs.api.responses.gate;


import com.qbros.acs.api.responses.QueryResp;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import lombok.Value;

@Value
public class GateSyncCompletedSignal implements QueryResp {

    GateID gateID;
    String gateName;
}
