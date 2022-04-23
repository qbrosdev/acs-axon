package com.qbros.acs.query.repositories.projections;

import lombok.Value;

import java.util.List;

@Value
public class GateTypesCount {

    List<GateTypeCountItem> gateTypeCountItems;
}
