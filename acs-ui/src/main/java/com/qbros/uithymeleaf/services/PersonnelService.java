package com.qbros.uithymeleaf.services;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.queries.personnel.AllPersonnelIdAndNameQuery;
import com.qbros.acs.api.queries.personnel.AllPersonnelSummaryQuery;
import com.qbros.acs.api.queries.personnel.PersonnelByIdQuery;
import com.qbros.acs.api.responses.personnel.PagedPersonnelSummary;
import com.qbros.acs.api.responses.personnel.PersonnelDetails;
import com.qbros.acs.api.responses.personnel.PersonnelNameAndId;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class PersonnelService extends AbsService {

    public PersonnelService(CommandGateway commandGateway, QueryGateway queryGateway, ResilientCaller resilientCaller) {
        super(commandGateway, queryGateway, resilientCaller);
    }

    public PersonnelDetails getById(PersonnelByIdQuery query) {
        return Try
                .of(() -> executeQuery(query, PersonnelDetails.class))
                .getOrElseThrow(this::translateException);
    }

    public PagedPersonnelSummary get(AllPersonnelSummaryQuery query) {
        return Try
                .of(() -> executeQuery(query, PagedPersonnelSummary.class))
                .getOrElseThrow(this::translateException);
    }

    public void executeSynchronousCommand(AbsCommand<PersonnelAggregateID> cmd) {
        Try
                .withResources(() -> subscribeForPersonnelUpdates(cmd.getAggregateID().getPersonnelID()))
                .of(subscription -> {
                    executeCommand(cmd);
                    return listenForFirstUpdate(subscription);
                })
                .getOrElseThrow(this::translateException);
    }

    public List<PersonnelNameAndId> getPersonnelSummaries(AllPersonnelIdAndNameQuery query) {
        return Try
                .of(() -> executeQuery(query, ResponseTypes.multipleInstancesOf(PersonnelNameAndId.class)))
                .getOrElseGet(throwable -> Collections.emptyList());
    }

    private SubscriptionQueryResult<PersonnelDetails, String> subscribeForPersonnelUpdates(PersonnelID personnelId) {
        return registerForResults(new PersonnelByIdQuery(personnelId), PersonnelDetails.class, String.class);
    }
}
