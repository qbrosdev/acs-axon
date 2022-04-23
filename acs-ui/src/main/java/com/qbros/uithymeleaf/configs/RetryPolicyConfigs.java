package com.qbros.uithymeleaf.configs;


import dev.failsafe.RetryPolicy;
import lombok.Data;

/**
 * Timeout and Retry policies
 */
@Data
public class RetryPolicyConfigs {

    private final RetryPolicy<Object> retryPolicy;
    private final int executionTimeoutInMilliSec;
}
