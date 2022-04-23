package com.qbros.acs.api.queries.report;

import com.qbros.acs.api.queries.AbsQuery;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GateTrafficQuery extends AbsQuery {

    int count;
    GateID gateID;
}
