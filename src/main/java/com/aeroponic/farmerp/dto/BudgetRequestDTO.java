package com.aeroponic.farmerp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BudgetRequestDTO {

    @NotNull(message = "Analytic Account ID is required")
    private Long analyticAccountId;

    @NotNull(message = "Allocated amount is required")
    @DecimalMin(value = "0.01", message = "Allocated amount must be positive")
    private BigDecimal allocatedAmount;

    @NotBlank(message = "Period is required")
    private String period;

    public Long getAnalyticAccountId() { return analyticAccountId; }
    public void setAnalyticAccountId(Long analyticAccountId) { this.analyticAccountId = analyticAccountId; }
    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
}
