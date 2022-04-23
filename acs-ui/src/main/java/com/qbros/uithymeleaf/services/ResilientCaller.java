package com.qbros.uithymeleaf.services;

import com.qbros.uithymeleaf.configs.RetryPolicyConfigs;
import dev.failsafe.Failsafe;
import dev.failsafe.function.ContextualSupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * Adds resiliency support to Command/Query executions.
 * The resiliency configuration for Command/Query is represented as {@link RetryPolicyConfigs} objects.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResilientCaller {

    /**
     * Resiliency configuration for {@link org.axonframework.commandhandling.gateway.CommandGateway}
     */
    protected final RetryPolicyConfigs commandRetryPolicyConfigs;
    /**
     * Resiliency configuration for {@link org.axonframework.queryhandling.QueryGateway}
     */
    protected final RetryPolicyConfigs queryRetryPolicyConfigs;

    @SuppressWarnings("unchecked")
    protected <R> R tryToExecuteQuery(ContextualSupplier<Object, R> supplier) {
        ContextualSupplier<Object, Object> supplier1 = supplier::get;
        return (R) Failsafe
                .with(queryRetryPolicyConfigs.getRetryPolicy())
                .get(supplier1);
    }

    @SuppressWarnings("unchecked")
    protected <R> R tryToExecuteCommand(ContextualSupplier<Object, R> supplier) {
        ContextualSupplier<Object, Object> supplier1 = supplier::get;
        return (R) Failsafe
                .with(commandRetryPolicyConfigs.getRetryPolicy())
                .get(supplier1);
    }

    protected int queryTimeoutIncrease(int attemptCount) {
        return attemptCount + queryRetryPolicyConfigs.getExecutionTimeoutInMilliSec();
    }

    protected int commandTimeoutIncrease(int attemptCount) {
        return attemptCount + commandRetryPolicyConfigs.getExecutionTimeoutInMilliSec();
    }

}
