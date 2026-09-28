package com.aeroponic.farmerp.dto;

import java.math.BigDecimal;

public class BudgetReportDTO {
    private Long analyticAccountId;
    private String zoneName;
    private String period;
    private BigDecimal allocatedAmount;
    private BigDecimal actualExpense;
    private BigDecimal variance;
    private Double percentageUsed;

    public BudgetReportDTO() {}

    public BudgetReportDTO(Long analyticAccountId, String zoneName, String period, BigDecimal allocatedAmount, BigDecimal actualExpense, BigDecimal variance, Double percentageUsed) {
        this.analyticAccountId = analyticAccountId;
        this.zoneName = zoneName;
        this.period = period;
        this.allocatedAmount = allocatedAmount;
        this.actualExpense = actualExpense;
        this.variance = variance;
        this.percentageUsed = percentageUsed;
    }

    public Long getAnalyticAccountId() { return analyticAccountId; }
    public void setAnalyticAccountId(Long analyticAccountId) { this.analyticAccountId = analyticAccountId; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }
    public BigDecimal getActualExpense() { return actualExpense; }
    public void setActualExpense(BigDecimal actualExpense) { this.actualExpense = actualExpense; }
    public BigDecimal getVariance() { return variance; }
    public void setVariance(BigDecimal variance) { this.variance = variance; }
    public Double getPercentageUsed() { return percentageUsed; }
    public void setPercentageUsed(Double percentageUsed) { this.percentageUsed = percentageUsed; }
}
