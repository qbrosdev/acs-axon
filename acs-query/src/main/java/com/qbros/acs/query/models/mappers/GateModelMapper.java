package com.qbros.acs.query.models.mappers;

import com.qbros.acs.api.events.gate.GateCreatedEvent;
import com.qbros.acs.api.responses.gate.GateDetails;
import com.qbros.acs.api.responses.gate.GateSummary;
import com.qbros.acs.api.responses.gate.GateSyncSummary;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.acs.query.models.GateEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GateModelMapper {

    public GateDetails toModel(GateEntity entity) {
        return new GateDetails(new GateID(entity.getGateID()),
                entity.getGateName(),
                entity.getGateType(),
                entity.getSyncStatus(),
                new ArrayList<>(entity.getAllowedPersonnel()));
    }

    public GateSyncSummary toGateSyncModel(List<GateEntity> list) {
        return new GateSyncSummary(
                list.stream()
                        .map(it -> Map.entry(it.getGateID(), it.getSyncStatus()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x)));
    }

    public GateSummary toSimpleModel(GateEntity entity) {
        return new GateSummary(new GateID(entity.getGateID()), entity.getGateName(), entity.getGateType(), entity.isActivated(), entity.getSyncStatus());
    }

    public GateEntity eventToEntity(GateCreatedEvent event) {
        return new GateEntity(event.getAggregateId().getGateID().stringValue(), event.getGateName(), event.getGateType(), event.getAllowedPersonnel());
    }
}
