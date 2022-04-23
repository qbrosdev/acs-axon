package com.qbros.uithymeleaf.configs;

import lombok.Data;

import java.util.List;

@Data
public class RetryConfigs {

    private int retryDelaySeconds = 5;
    private int maxRetries;
    private List<Class<? extends Throwable>> abortionTriggerExceptions;
    private List<Class<? extends Throwable>> handledExceptions;
    private int executionTimeoutInMilliSecond;
}
