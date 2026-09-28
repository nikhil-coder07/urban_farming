package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column(nullable = false)
    private Double humidity;

    @Column(name = "nutrient_ppm", nullable = false)
    private Double nutrientPpm;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "mist_triggered", nullable = false)
    private Boolean mistTriggered = false;

    public Telemetry() {}

    public Telemetry(Long id, Tower tower, Double humidity, Double nutrientPpm, LocalDateTime recordedAt, Boolean mistTriggered) {
        this.id = id;
        this.tower = tower;
        this.humidity = humidity;
        this.nutrientPpm = nutrientPpm;
        this.recordedAt = recordedAt != null ? recordedAt : LocalDateTime.now();
        this.mistTriggered = mistTriggered != null ? mistTriggered : false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Tower getTower() { return tower; }
    public void setTower(Tower tower) { this.tower = tower; }
    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }
    public Double getNutrientPpm() { return nutrientPpm; }
    public void setNutrientPpm(Double nutrientPpm) { this.nutrientPpm = nutrientPpm; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    public LocalDateTime getTimestamp() { return recordedAt; }
    public void setTimestamp(LocalDateTime timestamp) { this.recordedAt = timestamp; }

    public Boolean getMistTriggered() { return mistTriggered; }
    public void setMistTriggered(Boolean mistTriggered) { this.mistTriggered = mistTriggered; }
    public Boolean getMistCycleTriggered() { return mistTriggered; }
    public void setMistCycleTriggered(Boolean mistCycleTriggered) { this.mistTriggered = mistCycleTriggered; }
}
