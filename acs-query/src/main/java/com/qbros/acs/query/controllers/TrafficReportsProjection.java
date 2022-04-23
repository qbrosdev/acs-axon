package com.qbros.acs.query.controllers;

import com.qbros.acs.api.events.gate.GateAuthorizationFailEvent;
import com.qbros.acs.api.events.gate.GateAuthorizationSuccessEvent;
import com.qbros.acs.api.queries.report.GateTrafficQuery;
import com.qbros.acs.api.responses.reports.TrafficReport;
import com.qbros.acs.api.sharedmodels.BaseProjection;
import com.qbros.acs.query.models.TrafficReportEntity;
import com.qbros.acs.query.models.mappers.TrafficModelMapper;
import com.qbros.acs.query.repositories.TrafficReportRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrafficReportsProjection extends BaseProjection {

    private final TrafficReportRepository trafficReportRepository;
    private final QueryUpdateEmitter emitter;
    private final TrafficModelMapper gateModelMapper;

    @EventHandler
    public void on(GateAuthorizationSuccessEvent event) {
        Try.of(() -> gateModelMapper.eventToSuccessEntity(event))
                .andThen(trafficReportRepository::save)
                .andThen(this::signalGateTrafficQuery)
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(GateAuthorizationFailEvent event) {
        Try.of(() -> gateModelMapper.eventToFailEntity(event))
                .andThen(trafficReportRepository::save)
                .andThen(this::signalGateTrafficQuery)
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @QueryHandler
    public List<TrafficReport> handle(GateTrafficQuery query) {
        return Try.of(() -> trafficReportRepository
                        .findAllByGateIDOrderByTimestampDesc(query.getGateID().stringValue(), PageRequest.of(0, query.getCount()))
                        .stream()
                        .map(gateModelMapper::toTrafficReport)
                        .collect(Collectors.toList()))
                .getOrElseThrow(throwable -> translateException(query, throwable));
    }

    private void signalGateTrafficQuery(TrafficReportEntity entity) {
        emitter.emit(GateTrafficQuery.class,
                gateTrafficQuery -> gateTrafficQuery.getGateID().stringValue().equals(entity.getGateID()),
                gateModelMapper.toTrafficReport(entity));
    }
}
