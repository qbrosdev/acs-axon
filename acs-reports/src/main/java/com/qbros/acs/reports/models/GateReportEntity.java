package com.qbros.acs.reports.models;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class GateReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private String gateID;
    private GateType gateType;
    private long countSuccessPassages;
    private long countFailedPassages;
    private long gateActivity;

    public GateReportEntity(String gateID, GateType gateType, long countSuccessPassages, long countFailedPassages, long gateActivity) {
        this.gateID = gateID;
        this.gateType = gateType;
        this.countSuccessPassages = countSuccessPassages;
        this.countFailedPassages = countFailedPassages;
        this.gateActivity = gateActivity;
    }

    protected GateReportEntity() {
        //...
    }

}
