package com.qbros.acs.api.responses.reports;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import lombok.Value;

@Value
public class GateTypeItem {

    GateType gateType;
    int count;
}
