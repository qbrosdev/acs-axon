package com.qbros.uithymeleaf.models;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GateDetailsModel extends ViewModel {

    private String gateId;
    private String gateName;
    private Map<GateType, Boolean> gateType;
    private SyncStatus syncStatus = SyncStatus.SYNC;
    private List<Authorization> authorizationList;
    private List<String> options;
    private GateType selectedGateType;

    public GateDetailsModel(String gateId, String gateName, Map<GateType, Boolean> gateType, SyncStatus syncStatus, List<Authorization> authorizationList) {
        this.gateId = gateId;
        this.gateName = gateName;
        this.gateType = gateType;
        this.syncStatus = syncStatus;
        this.authorizationList = authorizationList;
        this.options = Collections.emptyList();
    }
}
