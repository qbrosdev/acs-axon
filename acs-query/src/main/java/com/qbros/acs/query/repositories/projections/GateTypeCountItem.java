package com.qbros.acs.query.repositories.projections;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import lombok.Value;

@Value
public class GateTypeCountItem {

    GateType gateType;
    long count;
}
