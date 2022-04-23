package com.qbros.uithymeleaf.configs;

import com.thoughtworks.xstream.XStream;
import dev.failsafe.RetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    @ConfigurationProperties("query.retry.conf")
    public RetryConfigs queryRetryConfig() {
        return new RetryConfigs();
    }

    @Bean
    @ConfigurationProperties("command.retry.conf")
    public RetryConfigs commandRetryConfig() {
        return new RetryConfigs();
    }

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{"com.qbros.acs.api.**"});
        return xStream;
    }

    @Bean("queryRetryPolicyConfigs")
    public RetryPolicyConfigs getQueryRetryPolicy() {

        RetryConfigs retryConfigs = queryRetryConfig();

        return new RetryPolicyConfigs(RetryPolicy.builder()
                .withDelay(Duration.ofSeconds(retryConfigs.getRetryDelaySeconds()))
                .abortOn(retryConfigs.getAbortionTriggerExceptions())
                .withMaxRetries(retryConfigs.getMaxRetries())
                .onAbort(e -> log.error("Retry aborted due to occurrence of {}", e.getException().getMessage(), e.getException()))
                .onRetry(e -> log.warn("Failure #{}. Retrying.", e.getAttemptCount()))
                .onFailedAttempt(e -> log.error("Query execution failed", e.getLastException()))
                .build(),
                retryConfigs.getExecutionTimeoutInMilliSecond());
    }

    @Bean("commandRetryPolicyConfigs")
    public RetryPolicyConfigs getCommandRetryPolicy() {

        RetryConfigs retryConfigs = commandRetryConfig();

        return new RetryPolicyConfigs(RetryPolicy.builder()
                .withDelay(Duration.ofSeconds(retryConfigs.getRetryDelaySeconds()))
                .abortOn(retryConfigs.getAbortionTriggerExceptions())
                .withMaxRetries(retryConfigs.getMaxRetries())
                .onAbort(e -> log.error("Retry aborted due to occurrence of {}", e.getException().getMessage(), e.getException()))
                .onRetry(e -> log.warn("Failure #{}. Retrying.", e.getAttemptCount()))
                .onFailedAttempt(e -> log.error("Command execution attempt failed", e.getLastException()))
                .build(),
                retryConfigs.getExecutionTimeoutInMilliSecond());
    }
}
