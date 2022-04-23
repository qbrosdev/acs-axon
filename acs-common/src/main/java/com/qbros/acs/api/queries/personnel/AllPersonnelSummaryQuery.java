package com.qbros.acs.api.queries.personnel;

import com.qbros.acs.api.queries.AbsQuery;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class AllPersonnelSummaryQuery extends AbsQuery {

    Integer offset;
    Integer limit;
}
