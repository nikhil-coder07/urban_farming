package com.aeroponic.farmerp.repository;

import com.aeroponic.farmerp.model.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    List<Telemetry> findByTowerIdOrderByRecordedAtDesc(Long towerId);

    @Query("SELECT t FROM Telemetry t WHERE t.id IN (SELECT MAX(t2.id) FROM Telemetry t2 GROUP BY t2.tower.id)")
    List<Telemetry> findLatestPerTower();
}
