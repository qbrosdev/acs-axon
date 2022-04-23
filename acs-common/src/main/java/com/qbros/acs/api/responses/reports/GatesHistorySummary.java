package com.qbros.acs.api.responses.reports;

import lombok.Value;

import java.util.List;

@Value
public class GatesHistorySummary {

    List<GateHistoryItem> summaryItems;
}
