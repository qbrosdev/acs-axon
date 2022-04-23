package com.qbros.acs.personnel;

import com.qbros.acs.api.commands.personnel.AddPersonnelCmd;
import com.qbros.acs.api.commands.personnel.DeletePersonnelCmd;
import com.qbros.acs.api.commands.personnel.EditPersonnelCmd;
import com.qbros.acs.api.commands.personnel.IsPersonnelPresentCmd;
import com.qbros.acs.api.events.gate.GateAuthorizationSuccessEvent;
import com.qbros.acs.api.events.personnel.PersonnelAddedEvent;
import com.qbros.acs.api.events.personnel.PersonnelDeletedEvent;
import com.qbros.acs.api.events.personnel.PersonnelEditedEvent;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@AllArgsConstructor
@Slf4j
@Aggregate(cache = "personnelCache")
public class Personnel {

    @AggregateIdentifier
    private PersonnelAggregateID personnelID;
    private boolean present;

    public Personnel() {
        // Required by Axon to construct an empty instance to initiate Event Sourcing.
    }

    @CommandHandler
    public Personnel(AddPersonnelCmd cmd) {
        log.info("Received Command {}", cmd);
        apply(new PersonnelAddedEvent(cmd.getAggregateID(), cmd.getFirstName(), cmd.getLastName(), cmd.getEmail()));
    }

    @CommandHandler
    public void handle(EditPersonnelCmd cmd) {
        log.info("Received Command {}", cmd);
        apply(new PersonnelEditedEvent(cmd.getAggregateID(), cmd.getFirstName(), cmd.getLastName(), cmd.getEmail()));
    }

    @CommandHandler
    public void handle(DeletePersonnelCmd cmd) {
        log.info("Received Command {}", cmd);
        apply(new PersonnelDeletedEvent(cmd.getAggregateID()));
    }

    @CommandHandler
    public boolean handle(IsPersonnelPresentCmd cmd) {
        log.info("Received Command {}", cmd);
        return present;
    }

    @EventSourcingHandler
    public void on(PersonnelAddedEvent event) {
        personnelID = event.getAggregateId();
        log.debug("Applied Event {} {}", event,this);
    }

    @EventSourcingHandler
    public void on(PersonnelDeletedEvent event) {
        markDeleted();
        log.debug("Applied Event {} {}", event,this);
    }

    @EventSourcingHandler
    public void on(GateAuthorizationSuccessEvent event) {
        switch (event.getResult().getAction()) {
            case EXIT:
                present = false;
                break;
            case ENTER:
                present = true;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + event.getResult().getAction());
        }
        log.debug("Applied Event {} {}", event,this);
    }
}
