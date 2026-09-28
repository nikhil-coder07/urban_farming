package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id", nullable = false)
    private AnalyticAccount analyticAccount;

    @Column(name = "allocated_amount", precision = 14, scale = 2, nullable = false)
    private BigDecimal allocatedAmount;

    @Column(length = 20, nullable = false)
    private String period;

    public Budget() {}

    public Budget(Long id, AnalyticAccount analyticAccount, BigDecimal allocatedAmount, String period) {
        this.id = id;
        this.analyticAccount = analyticAccount;
        this.allocatedAmount = allocatedAmount;
        this.period = period;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AnalyticAccount getAnalyticAccount() { return analyticAccount; }
    public void setAnalyticAccount(AnalyticAccount analyticAccount) { this.analyticAccount = analyticAccount; }
    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
}
