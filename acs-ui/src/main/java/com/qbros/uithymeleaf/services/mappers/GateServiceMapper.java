package com.qbros.uithymeleaf.services.mappers;

import com.qbros.acs.api.commands.gate.*;
import com.qbros.acs.api.queries.gate.GateDetailsByIdQuery;
import com.qbros.acs.api.responses.gate.GateDetails;
import com.qbros.acs.api.responses.gate.GateSummary;
import com.qbros.acs.api.responses.personnel.PersonnelNameAndId;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.uithymeleaf.models.Authorization;
import com.qbros.uithymeleaf.models.GateDetailsModel;
import com.qbros.uithymeleaf.models.GateSimpleModel;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GateServiceMapper {

    public GateDetailsModel toDetailedModel(GateDetails it, List<PersonnelNameAndId> personnelSummaries) {
        return new GateDetailsModel(it.getGateId().stringValue(), it.getGateName(), buildMap(it.getGateType()),
                it.getSyncStatus(), toAuthorization(it.getAllowedPersonnelIDs(), personnelSummaries));
    }

    public List<Authorization> toAuthorization(List<String> allowedPersonnelIds, List<PersonnelNameAndId> personnelSummaries) {
        Set<String> allowedIDs = new HashSet<>(allowedPersonnelIds);
        return personnelSummaries.stream()
                .map(it -> new Authorization(it.getPersonnelId(), it.getFullName(), allowedIDs.contains(it.getPersonnelId())))
                .collect(Collectors.toList());
    }

    public Map<GateType, Boolean> buildMap(GateType gateType) {
        return Arrays.stream(GateType.values())
                .map(gt -> Map.entry(gt, gateType == gt))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public CreateGateCmd toCreateGateCommand(GateDetailsModel gm) {
        return new CreateGateCmd(new GateAggregateID(gm.getGateId()),
                gm.getGateName(),
                gm.getSelectedGateType(),
                gm.getOptions() != null ? new ArrayList<>(gm.getOptions()) : Collections.emptyList());
    }

    public ModifyGateCmd toEditGateCommand(GateDetailsModel model) {
        return new ModifyGateCmd(new GateAggregateID(model.getGateId()),
                model.getGateName(),
                model.getSelectedGateType(),
                model.getOptions() != null ? new ArrayList<>(model.getOptions()) : Collections.emptyList());
    }

    public DeleteGateCmd getCmd(String perId) {
        return new DeleteGateCmd(new GateAggregateID(perId));
    }

    public InActivateGateCmd toInActivateGateCmd(String perId) {
        return new InActivateGateCmd(new GateAggregateID(perId));
    }

    public ActivateGateCmd toActivateGateCmd(String perId) {
        return new ActivateGateCmd(new GateAggregateID(perId));
    }

    public SyncGateCmd toSyncCmd(String perId) {
        return new SyncGateCmd(new GateAggregateID(perId));
    }

    public GateDetailsByIdQuery toGateDetailsByIdQuery(String idx) {
        return new GateDetailsByIdQuery(new GateID(idx));
    }

    public GateSimpleModel toGateSimpleModel(GateSummary item) {
        return new GateSimpleModel(item.getGate_id().stringValue(), item.getName(), item.getGateType(), item.isActive(), item.getSyncStatus());
    }

    public List<GateSimpleModel> toGateSimpleModelList(List<GateSummary> allSimpleSummary) {
        return allSimpleSummary
                .stream()
                .map(this::toGateSimpleModel)
                .collect(Collectors.toList());
    }

    private GateType buildMap2(Map<GateType, Boolean> gateType) {
        return gateType.entrySet().stream()
                .filter(entry -> entry.getValue().equals(true))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(GateType.TWO_WAYS);
    }
}
