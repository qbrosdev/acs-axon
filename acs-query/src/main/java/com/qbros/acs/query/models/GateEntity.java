package com.qbros.acs.query.models;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Gates")
@Slf4j
public class GateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private String gateID;
    private String gateName;
    private boolean activated = true;
    @Enumerated(EnumType.STRING)
    private GateType gateType;
    @Enumerated(EnumType.STRING)
    private SyncStatus syncStatus = SyncStatus.SYNC;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> allowedPersonnel;

    protected GateEntity() {
    }

    public GateEntity(String gateID, String gateName, GateType gateType, List<String> allowedPersonnel) {
        this.gateID = gateID;
        this.gateName = gateName;
        this.gateType = gateType;
        this.allowedPersonnel = allowedPersonnel;
    }
}
