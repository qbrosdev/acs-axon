package com.qbros.uithymeleaf.models;

import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GateSimpleModel extends ViewModel {

    private String gateId;
    private String gateName;
    private GateType gateType;
    private boolean active;
    private SyncStatus syncStatus;
}
