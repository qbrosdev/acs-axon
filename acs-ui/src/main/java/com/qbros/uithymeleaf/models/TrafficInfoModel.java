package com.qbros.uithymeleaf.models;

import com.google.type.DateTime;
import lombok.Data;

@Data
public class TrafficInfoModel {

    private final String personnelId;
    private final String gateId;
    private final DateTime timeStamp;
    private boolean wasSuccessful;
}
