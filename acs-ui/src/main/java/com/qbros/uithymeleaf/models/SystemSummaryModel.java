package com.qbros.uithymeleaf.models;

import lombok.Value;

@Value
public class SystemSummaryModel {

    int countTotalPersonnel;
    int countPresentPersonnel;
    int countTotalGates;
    int countActiveGates;
    int gateActivePercent;
    int personnelPresentPercent;
}
