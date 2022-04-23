package com.qbros.uithymeleaf.services;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.commands.gate.SyncGateCmd;
import com.qbros.acs.api.queries.gate.AllGatesSummaryQuery;
import com.qbros.acs.api.queries.gate.GateDetailsByIdQuery;
import com.qbros.acs.api.queries.gate.GateSyncQuery;
import com.qbros.acs.api.responses.gate.GateDetails;
import com.qbros.acs.api.responses.gate.GateSummary;
import com.qbros.acs.api.responses.gate.GateSyncCompletedSignal;
import com.qbros.acs.api.responses.gate.GateSyncSummary;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GatesService extends AbsService {

    public GatesService(CommandGateway commandGateway, QueryGateway queryGateway, ResilientCaller resilientCaller) {
        super(commandGateway, queryGateway, resilientCaller);
    }

    public GateDetails getById(GateDetailsByIdQuery query) {
        return Try
                .of(() -> executeQuery(query, GateDetails.class))
                .getOrElseThrow(this::translateException);
    }

    public void sync(SyncGateCmd cmd) {
        Try.run(() -> executeCommand(cmd))
                .getOrElseThrow(this::translateException);
    }

    public List<GateSummary> getAllSimpleSummary(AllGatesSummaryQuery allGatesSummaryQuery) {
        return Try
                .of(() -> executeQuery(allGatesSummaryQuery, ResponseTypes.multipleInstancesOf(GateSummary.class)))
                .getOrElseThrow(this::translateException);
    }

    public void executeSynchronousCommand(AbsCommand<GateAggregateID> cmd) {
        Try
                .withResources(() -> subscribeForGateUpdates(cmd.getAggregateID().getGateID()))
                .of(subscription -> {
                    executeCommand(cmd);
                    return listenForFirstUpdate(subscription);
                })
                .getOrElseThrow(this::translateException);
    }

    private SubscriptionQueryResult<GateDetails, String> subscribeForGateUpdates(GateID gateID) {
        return registerForResults(new GateDetailsByIdQuery(gateID),
                ResponseTypes.instanceOf(GateDetails.class),
                ResponseTypes.instanceOf(String.class));
    }

    public SubscriptionQueryResult<GateSyncSummary, GateSyncCompletedSignal> registerForSyncUpdates() {
        return registerForResults(new GateSyncQuery(), GateSyncSummary.class, GateSyncCompletedSignal.class);
    }
}
