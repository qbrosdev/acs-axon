package com.qbros.acs.api.events;

import com.qbros.acs.api.sharedmodels.domain.AggregateID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * The state of the target {@link org.axonframework.spring.stereotype.Aggregate}
 * will be reconstructed using {@link AbsEvent}
 *
 * @param <T> specifies the type of the target aggregate id.
 */
@EqualsAndHashCode(exclude = "timeStamp")
@Data
public abstract class AbsEvent<T extends AggregateID> implements Event {

    protected final Instant timeStamp = Instant.now();
    protected final String version = "1.0";
    protected final T aggregateId;
}
