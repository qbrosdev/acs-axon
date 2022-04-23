package com.qbros.acs.gate;

import com.qbros.acs.api.commands.gate.CreateGateCmd;
import com.qbros.acs.api.events.gate.GateCreatedEvent;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class GateTest {

    final FixtureConfiguration<Gate> testFixture = new AggregateTestFixture<>(Gate.class);
    @Mock
    SynchronisationService synchronisationService;
    GateAggregateID gateAggregateID = new GateAggregateID(new GateID("123"));
    CreateGateCmd createGateCMD = new CreateGateCmd(gateAggregateID, "ABC", GateType.ENTRANCE, List.of("124"));
    GateCreatedEvent gateCreatedEvent = new GateCreatedEvent(gateAggregateID, "ABC", GateType.ENTRANCE, List.of("124"));

    @BeforeEach
    void init() {
        testFixture.registerInjectableResource(synchronisationService);
    }

    @Test
    void handle() {
        testFixture.givenNoPriorActivity()
                .when(createGateCMD)
                .expectSuccessfulHandlerExecution()
                .expectEvents(gateCreatedEvent);
    }
}