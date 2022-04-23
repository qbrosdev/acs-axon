package com.qbros.uithymeleaf.services;

import com.qbros.acs.api.queries.report.GateTrafficQuery;
import com.qbros.acs.api.responses.reports.TrafficReport;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SentryService extends AbsService {

    public SentryService(CommandGateway commandGateway, QueryGateway queryGateway, ResilientCaller resilientCaller) {
        super(commandGateway, queryGateway, resilientCaller);
    }

    public SubscriptionQueryResult<List<TrafficReport>, TrafficReport> gateTraffic(GateTrafficQuery query) {
        return registerForResults(query,
                ResponseTypes.multipleInstancesOf(TrafficReport.class),
                ResponseTypes.instanceOf(TrafficReport.class));
    }
}
