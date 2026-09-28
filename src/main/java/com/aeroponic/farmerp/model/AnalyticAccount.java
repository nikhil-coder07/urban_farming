package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "analytic_accounts")
public class AnalyticAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Zone name is required")
    @Column(name = "zone_name", length = 100, nullable = false)
    private String zoneName;

    public AnalyticAccount() {}

    public AnalyticAccount(Long id, String zoneName) {
        this.id = id;
        this.zoneName = zoneName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
}
