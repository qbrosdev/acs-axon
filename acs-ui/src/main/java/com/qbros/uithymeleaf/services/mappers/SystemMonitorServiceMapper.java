package com.qbros.uithymeleaf.services.mappers;

import com.qbros.acs.api.responses.reports.*;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.uithymeleaf.models.GateTypesModel;
import com.qbros.uithymeleaf.models.GatesHistoryModel;
import com.qbros.uithymeleaf.models.SystemSummaryModel;
import com.qbros.uithymeleaf.services.Utils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SystemMonitorServiceMapper {

    public GatesHistoryModel getGatesSummaryModel(GatesHistorySummary gatesHistorySummary) {

        List<String> gateNameAndId = gatesHistorySummary.getSummaryItems().stream()
                .map(GateHistoryItem::getGateId)
                .collect(Collectors.toList());

        List<Long> gateTrafficCount = gatesHistorySummary.getSummaryItems().stream()
                .map(GateHistoryItem::getTotalActivity)
                .collect(Collectors.toList());

        return new GatesHistoryModel(gateNameAndId, gateTrafficCount, Utils.convertToString(Instant.now()));
    }

    public GateTypesModel toGateTypesModel(GateTypesSummary gateTypesSummary) {
        List<GateType> collect = gateTypesSummary.getGateTypeItemList().stream()
                .map(GateTypeItem::getGateType).collect(Collectors.toList());

        List<Integer> collect2 = gateTypesSummary.getGateTypeItemList().stream()
                .map(GateTypeItem::getCount).collect(Collectors.toList());

        return new GateTypesModel(collect, collect2, Utils.convertToString(Instant.now()));
    }

    public SystemSummaryModel toModel(SystemSummary summary) {

        int gateActivePercent = calculatePercentage(summary.getCountActiveGates(), summary.getCountTotalGates());
        int presentPersonnelPercent = calculatePercentage(summary.getCountPresentPersonnel(), summary.getCountTotalPersonnel());

        return new SystemSummaryModel(summary.getCountTotalPersonnel(),
                summary.getCountPresentPersonnel(),
                summary.getCountTotalGates(),
                summary.getCountActiveGates(),
                gateActivePercent,
                presentPersonnelPercent);
    }

    public int calculatePercentage(int obtained, int total) {
        return (total != 0) ? (int) ((obtained * 100.0f) / total) : 0;
    }
}
