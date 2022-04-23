package com.qbros.acs.reports.repositories;

import com.qbros.acs.reports.models.GateReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateReportRepository extends JpaRepository<GateReportEntity, Long> {

    Optional<GateReportEntity> findByGateID(String gateId);

    List<GateActivityProjection> findAllProjectedBy();

}
