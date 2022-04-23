package com.qbros.acs.api.responses.reports;

import com.qbros.acs.api.sharedmodels.domain.Action;
import lombok.Value;

@Value
public class TrafficReport {

    String gateId;
    String personnelId;
    Action action;
    String timeStamp;
    boolean wasSuccessful;
}
