package com.qbros.acs.api.responses.gate;


import com.qbros.acs.api.responses.QueryResp;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import lombok.Value;

import java.util.Map;

@Value
public class GateSyncSummary implements QueryResp {

    Map<String, SyncStatus> syncProgress;
}
