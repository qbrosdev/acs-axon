package com.qbros.acs.reports.configs;

import com.qbros.acs.api.configs.AbsBasicLoggingAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class BasicLoggingAspect extends AbsBasicLoggingAspect {

    @Around("@annotation(org.axonframework.queryhandling.QueryHandler)")
    public Object logQueries(ProceedingJoinPoint joinPoint) throws Throwable {
        return addLogs(joinPoint,
                jp -> log.debug("Query for {}", jp.getArgs()),
                (jp, result) -> log.debug("Query: '{}' Response: '{}'", jp.getSignature().toShortString(), result),
                (jp, throwable) -> log.error("exception {} in service layer with message {}", throwable.getMessage(), throwable.getStackTrace()));
    }

    @Around("@annotation(org.axonframework.eventhandling.EventHandler)")
    public Object logIncomingEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        return addLogs(joinPoint,
                jp -> log.debug("Event handling for {}", jp.getArgs()),
                (jp, result) -> log.debug("Event Method: '{}' Response: '{}'", jp.getSignature().toShortString(), result),
                (jp, throwable) -> log.error("Exception {} in event handler message {}", throwable.getMessage(), throwable.getStackTrace()));
    }
}



