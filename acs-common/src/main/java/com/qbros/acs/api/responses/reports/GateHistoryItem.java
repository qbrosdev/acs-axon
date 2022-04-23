package com.qbros.acs.api.responses.reports;

import lombok.Value;

@Value
public class GateHistoryItem {

    String gateId;
    long totalActivity;
}
