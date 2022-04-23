package com.qbros.uithymeleaf.models;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import lombok.Value;

import java.util.List;

@Value
public class GateTypesModel {

    List<GateType> gateTypes;
    List<Integer> count;
    String updateTime;
}
