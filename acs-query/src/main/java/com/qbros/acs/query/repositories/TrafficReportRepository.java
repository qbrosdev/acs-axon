package com.qbros.acs.query.repositories;

import com.qbros.acs.query.models.TrafficReportEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrafficReportRepository extends JpaRepository<TrafficReportEntity, Long> {

    List<TrafficReportEntity> findAllByGateIDOrderByTimestampDesc(String gateId, Pageable pageable);
}
