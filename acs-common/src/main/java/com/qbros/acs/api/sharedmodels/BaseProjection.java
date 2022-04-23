package com.qbros.acs.api.sharedmodels;

import com.qbros.acs.api.events.Event;
import com.qbros.acs.api.queries.Query;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public abstract class BaseProjection {

    protected RuntimeException translateException(Event event, Throwable throwable) {
        log.warn("Failed to handle event {} with error {}. View Model is not in sync with application status", event, throwable);
        return new IllegalStateException(String.format("Could not apply the event: %s to DB", event.toString()), throwable.getCause());
    }

    protected RuntimeException translateException(Query query, Throwable throwable) {
        log.warn("Failed to handle query {} with error {}", query, throwable);
        return new IllegalArgumentException(String.format("Could not handle query %s", query.toString()), throwable.getCause());
    }

    protected <T> T translateExceptionAndReturnDefault(Query query, Throwable throwable, Supplier<T> defaultGenerator) {
        log.warn("Failed to handle query {} with error {}", query, throwable);
        return defaultGenerator.get();
    }
}
