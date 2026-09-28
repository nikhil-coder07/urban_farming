package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.TelemetryRequestDTO;
import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.Telemetry;
import com.aeroponic.farmerp.model.Tower;
import com.aeroponic.farmerp.repository.TelemetryRepository;
import com.aeroponic.farmerp.repository.TowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetryService {

    @Autowired
    private TelemetryRepository telemetryRepository;

    @Autowired
    private TowerRepository towerRepository;

    public Telemetry recordTelemetry(TelemetryRequestDTO dto) {
        Tower tower = towerRepository.findById(dto.getTowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Tower not found with ID: " + dto.getTowerId()));

        boolean mistTriggered = dto.getHumidity() < 60.0 || dto.getNutrientPpm() < 800.0;

        Telemetry telemetry = new Telemetry();
        telemetry.setTower(tower);
        telemetry.setHumidity(dto.getHumidity());
        telemetry.setNutrientPpm(dto.getNutrientPpm());
        telemetry.setRecordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : LocalDateTime.now());
        telemetry.setMistTriggered(mistTriggered);

        return telemetryRepository.save(telemetry);
    }

    public List<Telemetry> getAllTelemetry() {
        return telemetryRepository.findAll();
    }

    public List<Telemetry> getLatestPerTower() {
        return telemetryRepository.findLatestPerTower();
    }

    public List<Telemetry> getHistoryByTower(Long towerId) {
        return telemetryRepository.findByTowerIdOrderByRecordedAtDesc(towerId);
    }
}
