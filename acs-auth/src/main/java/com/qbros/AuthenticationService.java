package com.qbros;

import com.qbros.acs.api.commands.gate.CheckAccessToGateCmd;
import com.qbros.acs.api.commands.personnel.IsPersonnelPresentCmd;
import com.qbros.acs.api.sharedmodels.domain.*;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public final CommandGateway commandGateway;

    //todo convert this into authentication flow saga
    public ActionResult authenticate(String gateId, String personnelId, Action action) {

        return Try
                .of(() -> CompletableFuture
                        .supplyAsync(() -> (boolean) commandGateway.sendAndWait(new IsPersonnelPresentCmd(new PersonnelAggregateID(personnelId))))
                        .<ActionResult>thenApply(isPresent -> {
                            log.debug("personnelId {}, present status {}", personnelId, isPresent);
                            return
                                    commandGateway
                                            .sendAndWait(new CheckAccessToGateCmd(new GateAggregateID(new GateID(gateId)), new PersonnelID(personnelId), isPresent, action));
                        })
                        .get(6, TimeUnit.SECONDS))
                .getOrElseGet(throwable -> {
                    log.warn("Exception {} in authentication", throwable.getMessage(), throwable);
                    return new ActionResult(action, false, throwable.getMessage());
                });
    }
}
