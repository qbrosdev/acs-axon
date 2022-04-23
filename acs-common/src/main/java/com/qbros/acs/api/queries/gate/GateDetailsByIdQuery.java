package com.qbros.acs.api.queries.gate;

import com.qbros.acs.api.queries.AbsQuery;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GateDetailsByIdQuery extends AbsQuery {

    GateID gateID;
}
