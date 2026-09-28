package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.dto.ProfitLossReportDTO;
import com.aeroponic.farmerp.model.CustomerInvoice;
import com.aeroponic.farmerp.model.PaymentStatus;
import com.aeroponic.farmerp.model.VendorBill;
import com.aeroponic.farmerp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class DashboardViewController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    @Autowired
    private VendorBillService vendorBillService;

    @Autowired
    private TowerService towerService;

    @Autowired
    private TelemetryService telemetryService;

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        // Profit & Loss
        try {
            ProfitLossReportDTO pl = reportService.getProfitAndLoss();
            model.addAttribute("totalRevenue", pl.getTotalIncome());
            model.addAttribute("totalExpenses", pl.getTotalExpenses());
        } catch (Exception e) {
            model.addAttribute("totalRevenue", BigDecimal.ZERO);
            model.addAttribute("totalExpenses", BigDecimal.ZERO);
        }

        // Open Invoices
        List<CustomerInvoice> invoices = customerInvoiceService.getAllInvoices();
        long openInvoicesCount = invoices.stream().filter(i -> i.getPaidStatus() == PaymentStatus.UNPAID).count();
        BigDecimal openInvoicesAmount = invoices.stream()
                .filter(i -> i.getPaidStatus() == PaymentStatus.UNPAID)
                .map(CustomerInvoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("openInvoicesCount", openInvoicesCount);
        model.addAttribute("openInvoicesAmount", openInvoicesAmount);

        // Open Bills
        List<VendorBill> bills = vendorBillService.getAllVendorBills();
        long openBillsCount = bills.stream().filter(b -> b.getPaidStatus() == PaymentStatus.UNPAID).count();
        BigDecimal openBillsAmount = bills.stream()
                .filter(b -> b.getPaidStatus() == PaymentStatus.UNPAID)
                .map(VendorBill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("openBillsCount", openBillsCount);
        model.addAttribute("openBillsAmount", openBillsAmount);

        // Active Towers & Telemetry
        model.addAttribute("activeTowersCount", towerService.getAllTowers().size());
        model.addAttribute("latestTelemetry", telemetryService.getLatestPerTower());

        return "dashboard";
    }
}
