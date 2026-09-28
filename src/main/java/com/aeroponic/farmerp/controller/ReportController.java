package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.BalanceSheetReportDTO;
import com.aeroponic.farmerp.dto.BudgetReportDTO;
import com.aeroponic.farmerp.dto.ProfitLossReportDTO;
import com.aeroponic.farmerp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/balance-sheet")
    public BalanceSheetReportDTO getBalanceSheet() {
        return reportService.getBalanceSheet();
    }

    @GetMapping("/profit-loss")
    public ProfitLossReportDTO getProfitAndLoss() {
        return reportService.getProfitAndLoss();
    }

    @GetMapping("/budget/{analyticAccountId}")
    public BudgetReportDTO getBudgetReport(@PathVariable Long analyticAccountId) {
        return reportService.getBudgetReport(analyticAccountId);
    }
}
