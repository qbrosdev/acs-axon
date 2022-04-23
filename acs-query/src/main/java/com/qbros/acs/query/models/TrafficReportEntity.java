package com.qbros.acs.query.models;

import com.qbros.acs.api.sharedmodels.domain.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Traffic")
public class TrafficReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private String gateID;
    private String personnelID;
    private Action action;
    private boolean successfulPass;
    private Instant timestamp;

    public TrafficReportEntity(String gateID, String personnelID, Action action, boolean successfulPass, Instant timestamp) {
        this.gateID = gateID;
        this.personnelID = personnelID;
        this.action = action;
        this.successfulPass = successfulPass;
        this.timestamp = timestamp;
    }

    protected TrafficReportEntity() {
    }

}
