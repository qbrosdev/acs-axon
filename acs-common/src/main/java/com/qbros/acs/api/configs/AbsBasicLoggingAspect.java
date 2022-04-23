package com.qbros.acs.api.configs;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbsBasicLoggingAspect {

    protected Object addLogs(ProceedingJoinPoint joinPoint,
                             Consumer<ProceedingJoinPoint> beforeLog,
                             BiConsumer<ProceedingJoinPoint, Object> afterLog,
                             BiConsumer<ProceedingJoinPoint, Throwable> exceptionLog) throws Throwable {
        Object proceed;
        try {
            beforeLog.accept(joinPoint);
            proceed = joinPoint.proceed();
            afterLog.accept(joinPoint, proceed);
        } catch (Throwable throwable) {
            exceptionLog.accept(joinPoint, throwable);
            throw throwable;
        }
        return proceed;
    }
}
