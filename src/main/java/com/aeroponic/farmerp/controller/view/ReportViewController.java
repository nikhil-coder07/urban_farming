package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.dto.BudgetReportDTO;
import com.aeroponic.farmerp.model.AnalyticAccount;
import com.aeroponic.farmerp.service.AnalyticAccountService;
import com.aeroponic.farmerp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportViewController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private AnalyticAccountService analyticAccountService;

    @GetMapping
    public String showReports(Model model) {
        // Balance Sheet
        model.addAttribute("balanceSheet", reportService.getBalanceSheet());

        // Profit & Loss
        model.addAttribute("profitLoss", reportService.getProfitAndLoss());

        // Budget vs Actual for all Analytic Accounts
        List<AnalyticAccount> analyticAccounts = analyticAccountService.getAllAnalyticAccounts();
        List<BudgetReportDTO> budgetReports = new ArrayList<>();
        for (AnalyticAccount aa : analyticAccounts) {
            try {
                budgetReports.add(reportService.getBudgetReport(aa.getId()));
            } catch (Exception ignored) {
            }
        }
        model.addAttribute("budgetReports", budgetReports);

        return "reports";
    }
}
