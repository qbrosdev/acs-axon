package com.qbros.acs.gate;

import com.qbros.acs.api.commands.gate.*;
import com.qbros.acs.api.events.gate.*;
import com.qbros.acs.api.sharedmodels.domain.*;
import com.qbros.acs.gate.internalcmd.MarkSyncCompletedInternalCommand;
import com.qbros.acs.gate.internalcmd.MarkSyncStartedInternalCommand;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashSet;
import java.util.Set;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@AllArgsConstructor
@ToString
@Slf4j
@Aggregate(cache = "gateCache")
public class Gate {

    private Set<String> registeredPersonnel;
    @AggregateIdentifier
    private GateAggregateID gateID;
    private GateType gateType;
    private boolean isActive = true;
    private Set<Action> gateSupportedActions = new HashSet<>();
    private SyncStatus syncStatus = SyncStatus.SYNC;
    private SynchronisationService synchronisationService;

    public Gate() {
        // Required by Axon to construct an empty instance to initiate Event Sourcing.
    }

    @CommandHandler
    public Gate(CreateGateCmd cmd) {
        log.info("Received Command {}", cmd);
        apply(new GateCreatedEvent(cmd.getAggregateID(), cmd.getGateName(), cmd.getGateType(), cmd.getAllowedPersonnel()));
    }

    @CommandHandler
    public void handle(ActivateGateCmd cmd) {
        log.info("Received Command {}", cmd);
        if (!isActive) {
            apply(new GateActivatedEvent(cmd.getAggregateID()));
        } else {
            throw new RuntimeException("Gate is already Active");
        }
    }

    @CommandHandler
    public void handle(InActivateGateCmd cmd) {
        log.info("Received Command {}", cmd);
        if (isActive) {
            apply(new GateInActivatedEvent(cmd.getAggregateID()));
        } else {
            throw new RuntimeException("Gate is already InActive");
        }
    }

    @CommandHandler
    public ActionResult checkAccess(CheckAccessToGateCmd cmd) {
        log.info("Received Command {}", cmd);
        checkActivation();
        ActionResult actionResult = getActionResult(cmd);
        if (actionResult.isSuccess()) {
            apply(new GateAuthorizationSuccessEvent(cmd.getAggregateID(), cmd.getPersonnelID(), actionResult));
        } else {
            apply(new GateAuthorizationFailEvent(cmd.getAggregateID(), cmd.getPersonnelID(), actionResult));
        }
        return actionResult;
    }

    @CommandHandler
    public void handle(ModifyGateCmd cmd) {
        log.info("Received Command {}", cmd);
        checkActivation();
        apply(new GateModifiedEvent(cmd.getAggregateID(), cmd.getGateName(), cmd.getGateType(), cmd.getAllowedPersonnel()));
    }

    @CommandHandler
    public void handle(SyncGateCmd cmd) {
        log.info("Received Command {}", cmd);
        checkSync();
        synchronisationService.performSync(cmd);
    }

    @CommandHandler
    public void handle(MarkSyncStartedInternalCommand cmd) {
        log.info("Sync Service Started!");
        apply(new GateSyncStartedEvent(cmd.getAggregateID()));
    }

    @CommandHandler
    public void handle(MarkSyncCompletedInternalCommand cmd) {
        log.info("Sync Service Completed!");
        apply(new GateSyncCompletedEvent(cmd.getAggregateID()));
    }

    @EventSourcingHandler
    public void on(GateCreatedEvent event, SynchronisationService synchronisationService) {
        this.synchronisationService = synchronisationService;
        log.info("Received Event {}", event);
        gateID = event.getAggregateId();
        switch (event.getGateType()) {
            case ENTRANCE:
                gateSupportedActions.add(Action.ENTER);
                break;
            case EXIT:
                gateSupportedActions.add(Action.EXIT);
                break;
            case TWO_WAYS:
                gateSupportedActions.add(Action.EXIT);
                gateSupportedActions.add(Action.ENTER);
                break;
        }
        registeredPersonnel = new HashSet<>(event.getAllowedPersonnel());
    }

    @EventSourcingHandler
    public void on(GateActivatedEvent event) {
        log.info("Applied Event {}", event);
        isActive = true;
    }

    @EventSourcingHandler
    public void on(GateInActivatedEvent event) {
        log.info("Applied Event {}", event);
        isActive = false;
    }

    @EventSourcingHandler
    public void on(GateModifiedEvent event) {
        registeredPersonnel = new HashSet<>(event.getAllowedPersonnel());
        gateType = event.getGateType();
        syncStatus = SyncStatus.NO_SYNC;
        log.debug("Applied Event {} {}", event, this);
    }

    @EventSourcingHandler
    public void on(GateSyncStartedEvent event) {
        syncStatus = SyncStatus.SYNCING;
        log.debug("Applied Event {}", event);
    }

    @EventSourcingHandler
    public void on(GateSyncCompletedEvent event) {
        syncStatus = SyncStatus.SYNC;
        log.debug("Applied Event {} {}", event, this);
    }

    private ActionResult getActionResult(CheckAccessToGateCmd cmd) {
        if (!gateSupportedActions.contains(cmd.getAction())) {
            return new ActionResult(cmd.getAction(), false, String.format("Unsupported Action %s", cmd.getAction()));
        } else if (!registeredPersonnel.contains(cmd.getPersonnelID().stringValue())) {
            return new ActionResult(cmd.getAction(), false, String.format("Personnel %s is not registered", cmd.getPersonnelID()));
        } else {
            switch (cmd.getAction()) {
                case ENTER:
                    return !cmd.isPersonnelPresent() ?
                            new ActionResult(cmd.getAction(), true) :
                            new ActionResult(cmd.getAction(), false, "Person is already present can't re-enter");
                case EXIT:
                    return cmd.isPersonnelPresent() ?
                            new ActionResult(cmd.getAction(), true) :
                            new ActionResult(cmd.getAction(), false, "Person is not present can't exit");
            }


            if (cmd.isPersonnelPresent()) {
                if (cmd.getAction().isEnter()) {
                    return new ActionResult(cmd.getAction(), false, String.format("Unsupported Action %s", cmd.getAction()));
                }
                return new ActionResult(cmd.getAction(), true);
            } else {
            }
            return new ActionResult(cmd.getAction(), true);
        }

    }

    private void checkActivation() {
        if (!isActive) {
            throw new RuntimeException("Gate is not Active");
        }
    }

    private void checkSync() {
        log.info("Sync value {}", syncStatus);
        if (SyncStatus.SYNC == syncStatus) {
            throw new RuntimeException("Gate is already Sync");
        }

        if (SyncStatus.SYNCING == syncStatus) {
            throw new RuntimeException("Gate is already Syncing");
        }
    }
}
