package com.aeroponic.farmerp.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProfitLossReportDTO {
    private List<AccountBalanceDTO> income;
    private BigDecimal totalIncome;
    private List<AccountBalanceDTO> expenses;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;

    public ProfitLossReportDTO() {}

    public ProfitLossReportDTO(List<AccountBalanceDTO> income, BigDecimal totalIncome, List<AccountBalanceDTO> expenses, BigDecimal totalExpenses, BigDecimal netProfit) {
        this.income = income;
        this.totalIncome = totalIncome;
        this.expenses = expenses;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
    }

    public List<AccountBalanceDTO> getIncome() { return income; }
    public void setIncome(List<AccountBalanceDTO> income) { this.income = income; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public List<AccountBalanceDTO> getExpenses() { return expenses; }
    public void setExpenses(List<AccountBalanceDTO> expenses) { this.expenses = expenses; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
}
