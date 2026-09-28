package com.aeroponic.farmerp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

public class TelemetryRequestDTO {

    @NotNull(message = "Tower ID is required")
    private Long towerId;

    @NotNull(message = "Humidity is required")
    @Range(min = 0, max = 100, message = "Humidity must be between 0 and 100")
    private Double humidity;

    @NotNull(message = "Nutrient PPM is required")
    @DecimalMin(value = "0.0", message = "Nutrient PPM must be positive")
    private Double nutrientPpm;

    private LocalDateTime recordedAt;

    public Long getTowerId() { return towerId; }
    public void setTowerId(Long towerId) { this.towerId = towerId; }
    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }
    public Double getNutrientPpm() { return nutrientPpm; }
    public void setNutrientPpm(Double nutrientPpm) { this.nutrientPpm = nutrientPpm; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
