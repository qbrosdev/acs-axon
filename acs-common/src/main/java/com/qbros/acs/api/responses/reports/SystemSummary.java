package com.qbros.acs.api.responses.reports;

import lombok.Value;

@Value
public class SystemSummary {

    int countTotalPersonnel;
    int countPresentPersonnel;
    int countTotalGates;
    int countActiveGates;
}
