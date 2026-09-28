package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.TelemetryRequestDTO;
import com.aeroponic.farmerp.model.Telemetry;
import com.aeroponic.farmerp.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aeroponics/telemetry")
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    @PostMapping
    public ResponseEntity<Telemetry> recordTelemetry(@Valid @RequestBody TelemetryRequestDTO dto) {
        return new ResponseEntity<>(telemetryService.recordTelemetry(dto), HttpStatus.CREATED);
    }

    @GetMapping("/latest")
    public List<Telemetry> getLatestPerTower() {
        return telemetryService.getLatestPerTower();
    }

    @GetMapping("/tower/{towerId}")
    public List<Telemetry> getHistoryByTower(@PathVariable Long towerId) {
        return telemetryService.getHistoryByTower(towerId);
    }
}
