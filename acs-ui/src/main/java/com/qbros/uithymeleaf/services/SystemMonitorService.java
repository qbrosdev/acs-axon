package com.qbros.uithymeleaf.services;

import com.qbros.acs.api.queries.report.GateTypesQuery;
import com.qbros.acs.api.queries.report.GatesHistoryQuery;
import com.qbros.acs.api.queries.report.SystemStatusQuery;
import com.qbros.acs.api.responses.reports.GateTypesSummary;
import com.qbros.acs.api.responses.reports.GatesHistorySummary;
import com.qbros.acs.api.responses.reports.SystemSummary;
import io.vavr.control.Try;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;

@Service
public class SystemMonitorService extends AbsService {

    public SystemMonitorService(CommandGateway commandGateway, QueryGateway queryGateway, ResilientCaller resilientCaller) {
        super(commandGateway, queryGateway, resilientCaller);
    }

    public SubscriptionQueryResult<SystemSummary, SystemSummary> registerForSystemSummary() {
        return registerForResults(new SystemStatusQuery(), SystemSummary.class, SystemSummary.class);
    }

    public GatesHistorySummary gatesSummary() {
        return Try.of(() -> executeQuery(new GatesHistoryQuery(), GatesHistorySummary.class))
                .getOrElseThrow(this::translateException);
    }

    public GateTypesSummary getGateTypes() {
        return Try.of(() -> executeQuery(new GateTypesQuery(), GateTypesSummary.class))
                .getOrElseThrow(this::translateException);
    }
}
