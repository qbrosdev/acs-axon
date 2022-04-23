package com.qbros.acs.query.repositories;

import com.qbros.acs.query.models.GateEntity;
import com.qbros.acs.query.repositories.projections.GateTypeCountItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<GateEntity, Long> {

    List<GateEntity> findAllByGateNameContainingOrGateIDContaining(String searchTerm, String searchTerm2);

    Optional<GateEntity> findByGateID(String gateId);

    @Query("SELECT new com.qbros.acs.query.repositories.projections.GateTypeCountItem(c.gateType, COUNT(c)) "
            + "FROM GateEntity AS c GROUP BY c.gateType")
    List<GateTypeCountItem> gateCountByType();

}