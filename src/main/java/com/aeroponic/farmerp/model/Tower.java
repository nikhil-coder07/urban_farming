package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "towers")
public class Tower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Zone name is required")
    @Column(name = "zone_name", length = 100, nullable = false)
    private String zoneName;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "led_spectrum", length = 50)
    private String ledSpectrum;

    public Tower() {}

    public Tower(Long id, String zoneName, Integer capacity, String ledSpectrum) {
        this.id = id;
        this.zoneName = zoneName;
        this.capacity = capacity;
        this.ledSpectrum = ledSpectrum;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getLedSpectrum() { return ledSpectrum; }
    public void setLedSpectrum(String ledSpectrum) { this.ledSpectrum = ledSpectrum; }
}
