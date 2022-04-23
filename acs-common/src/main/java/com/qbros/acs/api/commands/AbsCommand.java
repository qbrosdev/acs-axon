package com.qbros.acs.api.commands;

import com.qbros.acs.api.sharedmodels.domain.AggregateID;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Represents a command objects that will be sent to {@link org.axonframework.commandhandling.CommandBus}
 * via {@link org.axonframework.commandhandling.gateway.CommandGateway}
 * and will be handled by {@link org.axonframework.commandhandling.CommandHandler}
 *
 * @param <T> the type of target aggregate identifier of the command. It should extend {@link AggregateID}
 */
@Data
public abstract class AbsCommand<T extends AggregateID> implements Command {

    @TargetAggregateIdentifier
    final T aggregateId;

    public T getAggregateID() {
        return aggregateId;
    }
}
