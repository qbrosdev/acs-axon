package com.qbros.acs.reports.models;

import com.qbros.acs.api.events.gate.GateCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class GateModelMapper {

    public GateReportEntity eventToEntity(GateCreatedEvent event) {
        return new GateReportEntity(event.getAggregateId().getGateID().stringValue(), event.getGateType(), 0, 0, 0);
    }
}
