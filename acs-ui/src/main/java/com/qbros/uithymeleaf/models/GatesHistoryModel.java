package com.qbros.uithymeleaf.models;

import lombok.Value;

import java.util.List;

@Value
public class GatesHistoryModel {

    List<String> gateId;
    List<Long> trafficCount;
    String updateTime;
}
