package com.qbros.uithymeleaf.models;

import lombok.Value;

import java.util.List;

@Value
public class GatesTrafficCount {

    List<String> gateNames;
    List<Integer> gateTrafficCount;
}
