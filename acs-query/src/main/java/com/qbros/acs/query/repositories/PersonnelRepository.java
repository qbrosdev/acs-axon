package com.qbros.acs.query.repositories;

import com.qbros.acs.query.models.PersonnelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<PersonnelEntity, Long> {

    Optional<PersonnelEntity> findByPersonnelID(String personnelID);

    void deleteByPersonnelID(String personnelID);
}
