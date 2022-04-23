package com.qbros.uithymeleaf.services.mappers;

import com.qbros.acs.api.responses.personnel.PersonnelNameAndId;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.uithymeleaf.models.Authorization;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.qbros.acs.api.sharedmodels.domain.GateType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GateServiceMapperTest {

    GateServiceMapper mapper = new GateServiceMapper();

    private static Stream<Arguments> inputForBuildMap() {
        return Stream.of(
                Arguments.of(ENTRANCE, Map.of(ENTRANCE, true, EXIT, false, TWO_WAYS, false)),
                Arguments.of(ENTRANCE, Map.of(ENTRANCE, true, EXIT, false, TWO_WAYS, false)),
                Arguments.of(ENTRANCE, Map.of(ENTRANCE, true, EXIT, false, TWO_WAYS, false))
        );
    }

    private static Stream<Arguments> inputToAuthorization() {

        List<PersonnelNameAndId> list = List.of(
                new PersonnelNameAndId("123", "NAME1"),
                new PersonnelNameAndId("456", "NAME2"));

        return Stream.of(
                Arguments.of(List.of("123"),
                        list,
                        List.of(
                                new Authorization("123", "NAME1", true),
                                new Authorization("456", "NAME2", false))),

                Arguments.of(List.of("456"),
                        list,
                        List.of(
                                new Authorization("123", "NAME1", false),
                                new Authorization("456", "NAME2", true))));
    }

    @ParameterizedTest
    @MethodSource("inputForBuildMap")
    void testBuildMap(GateType input, Map<GateType, Boolean> expected) {
        assertEquals(mapper.buildMap(input), expected);
    }

    @ParameterizedTest
    @MethodSource("inputToAuthorization")
    void testToAuthorization(List<String> allowedPersonnelIds,
                             List<PersonnelNameAndId> personnelSummaries,
                             List<Authorization> expected) {

        assertThat(mapper.toAuthorization(allowedPersonnelIds, personnelSummaries))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

}