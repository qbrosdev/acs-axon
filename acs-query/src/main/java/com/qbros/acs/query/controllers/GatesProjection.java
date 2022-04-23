package com.qbros.acs.query.controllers;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.events.gate.*;
import com.qbros.acs.api.queries.gate.AllGatesSummaryQuery;
import com.qbros.acs.api.queries.gate.GateDetailsByIdQuery;
import com.qbros.acs.api.queries.gate.GateSyncQuery;
import com.qbros.acs.api.queries.report.GateTypesQuery;
import com.qbros.acs.api.responses.gate.GateDetails;
import com.qbros.acs.api.responses.gate.GateSummary;
import com.qbros.acs.api.responses.gate.GateSyncCompletedSignal;
import com.qbros.acs.api.responses.gate.GateSyncSummary;
import com.qbros.acs.api.responses.reports.GateTypeItem;
import com.qbros.acs.api.responses.reports.GateTypesSummary;
import com.qbros.acs.api.sharedmodels.BaseProjection;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import com.qbros.acs.query.models.GateEntity;
import com.qbros.acs.query.models.mappers.GateModelMapper;
import com.qbros.acs.query.repositories.GateRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatesProjection extends BaseProjection {

    private final GateRepository gateRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;
    private final GateModelMapper gateModelMapper;

    @EventHandler
    public void on(GateCreatedEvent event) {
        Try.of(() -> gateModelMapper.eventToEntity(event))
                .andThen(gateRepository::save)
                .andThen(entity -> emmitSignalForGateById(entity.getGateID()))
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateModifiedEvent event) {
        tryToFetchGate(getGateID(event))
                .mapTry(entity -> {
                    entity.setGateName(event.getGateName());
                    entity.setGateType(event.getGateType());
                    entity.setSyncStatus(SyncStatus.NO_SYNC);
                    entity.setAllowedPersonnel(event.getAllowedPersonnel());
                    return entity;
                })
                .andThen(entity -> emmitSignalForGateById(entity.getGateID()))
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateInActivatedEvent event) {
        tryToFetchGate(getGateID(event))
                .mapTry(entity -> {
                    entity.setActivated(false);
                    return entity;
                })
                .andThen(entity -> emmitSignalForGateById(entity.getGateID()))
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateActivatedEvent event) {
        tryToFetchGate(getGateID(event))
                .mapTry(entity -> {
                    entity.setActivated(true);
                    return entity;
                })
                .andThen(entity -> emmitSignalForGateById(entity.getGateID()))
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateSyncStartedEvent event) {
        tryToFetchGate(getGateID(event))
                .mapTry(entity -> {
                    entity.setSyncStatus(SyncStatus.SYNCING);
                    return entity;
                })
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateSyncCompletedEvent event) {
        tryToFetchGate(getGateID(event))
                .mapTry(entity -> {
                    entity.setSyncStatus(SyncStatus.SYNC);
                    return entity;
                })
                .andThen(entity -> {
                    emmitSignalForGateSync(entity.getGateID(), entity.getGateName());
                })
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @QueryHandler
    public List<GateSummary> handle(AllGatesSummaryQuery query) {
        return Try
                .of(gateRepository::findAll)
                .mapTry(gateEntities -> gateEntities.stream().map(gateModelMapper::toSimpleModel).collect(Collectors.toList()))
                .getOrElseGet(throwable -> translateExceptionAndReturnDefault(query, throwable, Collections::emptyList));
    }

    @QueryHandler
    GateSyncSummary handle(GateSyncQuery query) {
        return Try
                .of(gateRepository::findAll)
                .mapTry(gateModelMapper::toGateSyncModel)
                .getOrElseGet(throwable -> translateExceptionAndReturnDefault(query, throwable, () -> new GateSyncSummary(Collections.emptyMap())));
    }

    @QueryHandler
    public GateDetails handle(GateDetailsByIdQuery query) {
        return Try.of(() ->
                        gateRepository
                                .findByGateID(query.getGateID().stringValue())
                                .map(gateModelMapper::toModel)
                                .orElseThrow())
                .getOrElseThrow(throwable -> translateException(query, throwable));
    }

    @QueryHandler
    public GateTypesSummary handle(GateTypesQuery query) {
        return Try.of(() ->
                        gateRepository
                                .gateCountByType().stream()
                                .map(it -> new GateTypeItem(it.getGateType(), (int) it.getCount()))
                                .collect(Collectors.toList()))
                .mapTry(GateTypesSummary::new)
                .getOrElseGet(throwable ->
                        translateExceptionAndReturnDefault(query, throwable, () -> new GateTypesSummary(Collections.emptyList())));
    }

    private String getGateID(AbsEvent<GateAggregateID> event) {
        return event.getAggregateId().getGateID().stringValue();
    }

    private void emmitSignalForGateById(String gateID) {
        queryUpdateEmitter
                .emit(GateDetailsByIdQuery.class, query -> query.getGateID().stringValue().equals(gateID), gateID);
    }

    private void emmitSignalForGateSync(String gateID, String gateName) {
        queryUpdateEmitter
                .emit(GateSyncQuery.class, gateSyncQuery -> true, new GateSyncCompletedSignal(new GateID(gateID), gateName));
    }

    private Try<GateEntity> tryToFetchGate(String gateID) {
        return Try.of(() -> gateRepository
                .findByGateID(gateID)
                .orElseThrow(() -> new RuntimeException("Entity not found")));
    }
}
