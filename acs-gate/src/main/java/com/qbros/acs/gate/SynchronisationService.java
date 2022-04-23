package com.qbros.acs.gate;

import com.qbros.acs.api.commands.gate.SyncGateCmd;
import com.qbros.acs.gate.internalcmd.MarkSyncCompletedInternalCommand;
import com.qbros.acs.gate.internalcmd.MarkSyncStartedInternalCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for update the physical gate controller devices with
 * the latest changes in the gate state.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SynchronisationService {

    private final CommandGateway commandGateway;

    @Async
    public void performSync(SyncGateCmd cmd) {
        commandGateway.send(new MarkSyncStartedInternalCommand(cmd.getAggregateID()));
        syncLogic();
        commandGateway.send(new MarkSyncCompletedInternalCommand(cmd.getAggregateID()));
    }

    /**
     * Current implementation just waits for some time but in real scenario we have to send
     * the state changes to the gate controller (via FTP, ...).
     */
    private void syncLogic() {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
