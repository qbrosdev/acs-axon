package com.qbros.uithymeleaf.services;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.queries.AbsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * This class abstracts away the details on how to communicate with the Command/Query handlers.
 * The public API of the Service classes should only accept object of type {@link AbsQuery} for query operations
 * and object of type {@link AbsCommand} for command operations
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbsService {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final ResilientCaller resilientCaller;

    protected ServiceLayerException translateException(Throwable t) {
        log.warn("Error {}", t.getMessage(), t.getCause());
        return new ServiceLayerException(String.format("Service Exception, Cause:  [ %s ]", t.getMessage()), t.getCause());
    }

    protected <Q, I, U> SubscriptionQueryResult<I, U> registerForResults(Q query, Class<I> initialResponseType, Class<U> updateResponseType) {
        return queryGateway.subscriptionQuery(query, initialResponseType, updateResponseType);
    }

    protected <Q, I, U> SubscriptionQueryResult<I, U> registerForResults(Q query, ResponseType<I> initialResponseType, ResponseType<U> updateResponseType) {
        return queryGateway.subscriptionQuery(query, initialResponseType, updateResponseType);
    }

    protected <R> R executeCommand(AbsCommand<?> command, Class<R> responseType) {
        return resilientCaller.tryToExecuteCommand(context -> {
            log.info("Processing Command: {}", command);
            return commandGateway.sendAndWait(command, 10, TimeUnit.MILLISECONDS);
        });
    }

    protected void executeCommand(AbsCommand<?> command) {
        resilientCaller.tryToExecuteCommand(context ->
                commandGateway
                        .sendAndWait(command, resilientCaller.commandTimeoutIncrease(context.getAttemptCount()), TimeUnit.MILLISECONDS));
    }

    protected <R> R executeQuery(AbsQuery absQuery, Class<R> responseType) {
        return resilientCaller.tryToExecuteQuery(context -> {
            log.info("Processing Query: {}", absQuery);
            return queryGateway
                    .query(absQuery, responseType)
                    .get(resilientCaller.queryTimeoutIncrease(context.getAttemptCount()), TimeUnit.MILLISECONDS);
        });
    }

    protected <R> R executeQuery(AbsQuery absQuery, ResponseType<R> responseType) {
        return resilientCaller.tryToExecuteQuery(context -> {
            log.info("Processing Query: {}", absQuery);
            return queryGateway
                    .query(absQuery, responseType)
                    .get(resilientCaller.queryTimeoutIncrease(context.getAttemptCount()), TimeUnit.MILLISECONDS);
        });
    }

    protected <I, U> U listenForFirstUpdate(SubscriptionQueryResult<I, U> subscriptionQuery) {
        return resilientCaller.tryToExecuteQuery(context ->
                subscriptionQuery
                        .updates()
                        .blockFirst(Duration.ofSeconds(resilientCaller.queryTimeoutIncrease(context.getAttemptCount()))));
    }

}
