package com.qbros.acs.reports.controllers;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.events.gate.GateAuthorizationFailEvent;
import com.qbros.acs.api.events.gate.GateAuthorizationSuccessEvent;
import com.qbros.acs.api.events.gate.GateCreatedEvent;
import com.qbros.acs.api.queries.report.GatesHistoryQuery;
import com.qbros.acs.api.responses.reports.GateHistoryItem;
import com.qbros.acs.api.responses.reports.GatesHistorySummary;
import com.qbros.acs.api.sharedmodels.BaseProjection;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.reports.models.GateModelMapper;
import com.qbros.acs.reports.models.GateReportEntity;
import com.qbros.acs.reports.repositories.GateReportRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GateReportsProjection extends BaseProjection {

    private final GateReportRepository gateReportRepository;
    private final GateModelMapper gateModelMapper;

    @EventHandler
    public void on(GateCreatedEvent event) {
        Try.of(() -> gateModelMapper.eventToEntity(event))
                .andThen(gateReportRepository::save)
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateAuthorizationSuccessEvent event) {
        updateGateReport(event,
                entity -> {
                    entity.setCountSuccessPassages(entity.getCountSuccessPassages() + 1);
                    entity.setGateActivity(entity.getGateActivity() + 1);
                    return entity;
                });
    }

    @EventHandler
    public void on(GateAuthorizationFailEvent event) {
        updateGateReport(event,
                entity -> {
                    entity.setCountFailedPassages(entity.getCountFailedPassages() + 1);
                    entity.setGateActivity(entity.getGateActivity() + 1);
                    return entity;
                });
    }

    @QueryHandler
    public GatesHistorySummary handle(GatesHistoryQuery query) {
        return Try.of(() -> gateReportRepository.findAllProjectedBy().stream()
                        .map(it -> new GateHistoryItem(it.getGateID(), it.getGateActivity()))
                        .collect(Collectors.toList()))
                .mapTry(GatesHistorySummary::new)
                .getOrElseThrow(throwable -> translateException(query, throwable));
    }

    private void updateGateReport(AbsEvent<GateAggregateID> event, Function<GateReportEntity, GateReportEntity> fun) {
        Try.of(() -> gateReportRepository
                        .findByGateID(event.getAggregateId().getGateID().stringValue())
                        .map(fun)
                        .orElseThrow(() -> new RuntimeException("Entity not found")))
                .andThen(gateReportRepository::save)
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }
}
