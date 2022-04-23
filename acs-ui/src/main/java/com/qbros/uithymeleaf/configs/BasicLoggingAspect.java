package com.qbros.uithymeleaf.configs;

import com.qbros.acs.api.configs.AbsBasicLoggingAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class BasicLoggingAspect extends AbsBasicLoggingAspect {

    //    @Before("@within(org.springframework.stereotype.Controller) && execution(public * * (..)) && within(com.qbros.uithymeleaf.controllers.AbsController+)")
//    @Before("@within(org.springframework.stereotype.Controller) && execution(public * * (..))")
    @Before("execution(public * com.qbros.uithymeleaf.controllers.manager.GatesMgntCtrl.* (..))")
    public void logApiCalls(JoinPoint joinPoint) throws Throwable {
        log.debug("Calling... API Method:'{}' with args: '{}'", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @Around("@within(org.springframework.stereotype.Service) && execution(public * * (..)) && within(com.qbros.uithymeleaf.services.AbsService+)")
    public Object logServiceCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        return addLogs(joinPoint,
                jp -> log.debug("Calling Service Method:'{}' with arguments: '{}'", jp.getSignature().toShortString(), jp.getArgs()),
                (jp, result) -> log.debug("Service Method: '{}' Response: '{}'", jp.getSignature().toShortString(), result),
                (jp, throwable) -> log.error("exception {} in service layer with message {}",
                        throwable.getMessage(), throwable.getStackTrace()));
    }

}



