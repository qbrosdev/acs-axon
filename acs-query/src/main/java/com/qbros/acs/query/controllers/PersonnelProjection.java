package com.qbros.acs.query.controllers;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.events.personnel.PersonnelAddedEvent;
import com.qbros.acs.api.events.personnel.PersonnelDeletedEvent;
import com.qbros.acs.api.events.personnel.PersonnelEditedEvent;
import com.qbros.acs.api.queries.personnel.AllPersonnelIdAndNameQuery;
import com.qbros.acs.api.queries.personnel.AllPersonnelSummaryQuery;
import com.qbros.acs.api.queries.personnel.PersonnelByIdQuery;
import com.qbros.acs.api.responses.PagedSummary;
import com.qbros.acs.api.responses.personnel.PagedPersonnelSummary;
import com.qbros.acs.api.responses.personnel.PersonnelCountUpdatedSignal;
import com.qbros.acs.api.responses.personnel.PersonnelDetails;
import com.qbros.acs.api.responses.personnel.PersonnelNameAndId;
import com.qbros.acs.api.sharedmodels.BaseProjection;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import com.qbros.acs.query.models.PersonnelEntity;
import com.qbros.acs.query.models.mappers.PersonnelModelMapper;
import com.qbros.acs.query.repositories.PersonnelRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonnelProjection extends BaseProjection {

    private final PersonnelRepository personnelRepository;
    private final QueryUpdateEmitter emitter;
    private final PersonnelModelMapper personnelModelMapper;

    @EventHandler
    public void on(PersonnelAddedEvent event) {
        Try.of(() -> personnelModelMapper.eventToEntity(event))
                .andThen(personnelRepository::save)
                .andThen(() -> {
                    emmitSignalForAllPersonnelQuery();
                    emmitSignalForPersonnelById(extractPersonnelIDValue(event));
                })
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(PersonnelEditedEvent event) {
        Try.of(() -> personnelRepository.findByPersonnelID(extractPersonnelIDValue(event))
                        .map(foundEntity -> applyChanges(event, foundEntity))
                        .orElseThrow(() -> new RuntimeException("Entity not found")))
                .andThen(personnelRepository::save)
                .andThen(() -> emmitSignalForPersonnelById(extractPersonnelIDValue(event)))
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @EventHandler
    public void on(PersonnelDeletedEvent event) {
        Try.run(() -> personnelRepository.deleteByPersonnelID(extractPersonnelIDValue(event)))
                .andThen(() -> emmitSignalForPersonnelById(extractPersonnelIDValue(event)))
                .getOrElseThrow(throwable -> translateException(event, throwable));
    }

    @QueryHandler
    public PagedPersonnelSummary handle(AllPersonnelSummaryQuery query) {
        return Try.of(() -> personnelRepository
                        .findAll(PageRequest.of(query.getOffset(), query.getLimit()).withSort(Sort.by("idx")))
                        .map(personnelModelMapper::toModel))
                .map(PagedPersonnelSummary::new)
                .getOrElseGet(throwable ->
                        translateExceptionAndReturnDefault(query, throwable,
                                () -> (PagedPersonnelSummary) PagedSummary.emptyPage()));
    }

    @QueryHandler
    public List<PersonnelNameAndId> handle(AllPersonnelIdAndNameQuery query) {
        return Try.of(() -> personnelRepository
                        .findAll(Sort.by("idx")).stream().map(personnelModelMapper::toSimpleModel)
                        .collect(Collectors.toList()))
                .getOrElseGet(throwable ->
                        translateExceptionAndReturnDefault(query, throwable, Collections::emptyList));
    }

    @QueryHandler
    public PersonnelDetails handle(PersonnelByIdQuery query) {
        return Try.of(() -> personnelRepository
                        .findByPersonnelID(query.getPersonnelId().stringValue())
                        .map(personnelModelMapper::toModel)
                        .orElseThrow())
                .getOrElseThrow(throwable -> translateException(query, throwable));
    }

    private String extractPersonnelIDValue(AbsEvent<PersonnelAggregateID> event) {
        return event.getAggregateId().getPersonnelID().stringValue();
    }

    private void emmitSignalForAllPersonnelQuery() {
        emitter.emit(
                AllPersonnelSummaryQuery.class,
                query -> {
                    log.info("Emitting new Personnel {} to registered query '{}'", query, AllPersonnelSummaryQuery.class.getSimpleName());
                    return true;
                },
                new PersonnelCountUpdatedSignal());
    }

    private void emmitSignalForPersonnelById(String personnelID) {
        emitter
                .emit(PersonnelByIdQuery.class, query -> personnelID.equals(query.getPersonnelId().stringValue()), personnelID);
    }

    private PersonnelEntity applyChanges(PersonnelEditedEvent event, PersonnelEntity entity) {
        entity.setFirstName(event.getFirstName().getValue());
        entity.setLastName(event.getLastName().getValue());
        entity.setEmail(event.getEmail().getValue());
        return entity;
    }
}
