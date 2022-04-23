package com.qbros.acs.query.controllers;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.events.gate.GateActivatedEvent;
import com.qbros.acs.api.events.gate.GateAuthorizationSuccessEvent;
import com.qbros.acs.api.events.gate.GateCreatedEvent;
import com.qbros.acs.api.events.gate.GateInActivatedEvent;
import com.qbros.acs.api.events.personnel.PersonnelAddedEvent;
import com.qbros.acs.api.queries.report.SystemStatusQuery;
import com.qbros.acs.api.responses.reports.SystemSummary;
import com.qbros.acs.api.sharedmodels.BaseProjection;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A lightweight view of the state of the system, including the state of gates and state of the personnel.
 * This service is used by the UI Dashboard and notifies the UI about the state changes in the sytem.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemStatusProjection extends BaseProjection {

    /**
     * Map showing which personnel ids are present or absent.
     * Value {@code True} for map value, means that person is present
     */
    private final Map<String, Boolean> personnelPresentStatus = new ConcurrentHashMap<>();
    /**
     * Map showing which gates are active or not.
     */
    private final Map<String, Boolean> gateStatus = new ConcurrentHashMap<>();
    private final QueryUpdateEmitter emitter;

    @EventHandler
    public void on(PersonnelAddedEvent event) {
        log.info("Event came {}", event);
        //all new personnel are considered not present.
        personnelPresentStatus.put(extractPersonnelId(event), false);
        signalSystemStatusQuery();
    }

    @EventHandler
    public void on(GateAuthorizationSuccessEvent event) {
        log.info("Event came {}", event);
        modifyPresentStatus(event);
        signalSystemStatusQuery();
    }

    @EventHandler
    public void on(GateCreatedEvent event) {
        log.info("Event came {}", event);
        gateStatus.put(extractGateID(event), true);
        signalSystemStatusQuery();
    }

    @EventHandler
    public void on(GateActivatedEvent event) {
        log.info("Event came {}", event);
        gateStatus.put(extractGateID(event), true);
        signalSystemStatusQuery();
    }

    @EventHandler
    public void on(GateInActivatedEvent event) {
        log.info("Event came {}", event);
        gateStatus.put(extractGateID(event), false);
        signalSystemStatusQuery();
    }

    @QueryHandler
    public SystemSummary handle(SystemStatusQuery query) {
        log.info("Query came {}", query);
        return getSystemSummary();
    }

    private void signalSystemStatusQuery() {
        log.info("Update Signal");
        emitter.emit(SystemStatusQuery.class, query -> true, getSystemSummary());
    }

    private SystemSummary getSystemSummary() {
        int totalPersonnel = personnelPresentStatus.size();
        //Only personnel with value present in the map are considered present.
        int presentPersonnel = (int) personnelPresentStatus.values().stream().filter(present -> present).count();
        int totalGates = gateStatus.size();
        int activeGates = (int) gateStatus.values().stream().filter(active -> active).count();
        return new SystemSummary(totalPersonnel, presentPersonnel, totalGates, activeGates);
    }

    private void modifyPresentStatus(GateAuthorizationSuccessEvent event) {
        switch (event.getResult().getAction()) {
            case EXIT:
                personnelPresentStatus.put(event.getPersonnelID().stringValue(), false);
                break;
            case ENTER:
                personnelPresentStatus.put(event.getPersonnelID().stringValue(), true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + event.getResult().getAction());
        }
    }

    private String extractPersonnelId(AbsEvent<PersonnelAggregateID> event) {
        return event.getAggregateId().getPersonnelID().stringValue();
    }

    private String extractGateID(AbsEvent<GateAggregateID> event) {
        return event.getAggregateId().getGateID().stringValue();
    }

}
