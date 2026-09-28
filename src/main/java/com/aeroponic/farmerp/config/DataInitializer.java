package com.aeroponic.farmerp.config;

import com.aeroponic.farmerp.model.*;
import com.aeroponic.farmerp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private TowerRepository towerRepository;

    @Autowired
    private TelemetryRepository telemetryRepository;

    @Autowired
    private AnalyticAccountRepository analyticAccountRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public void run(String... args) throws Exception {
        if (contactRepository.count() == 0) {
            contactRepository.save(new Contact(null, "FreshMart Retail Chain", ContactType.CUSTOMER, "101 Market Rd, Metro City", "9876543210"));
            contactRepository.save(new Contact(null, "GreenLeaf Supermarkets", ContactType.CUSTOMER, "45 Bio Way, North Suburb", "9876543211"));
            contactRepository.save(new Contact(null, "NutriGrow Chemical Supplies", ContactType.VENDOR, "789 Industrial Estate, West Hub", "9876543212"));
        }

        if (productRepository.count() == 0) {
            productRepository.save(new Product(null, "Organic Aeroponic Lettuce", ProductType.GOODS, new BigDecimal("120.00"), "KG"));
            productRepository.save(new Product(null, "Concentrated Mineral Solution", ProductType.GOODS, new BigDecimal("850.00"), "UNIT"));
            productRepository.save(new Product(null, "Misting Nozzle Assembly", ProductType.GOODS, new BigDecimal("450.00"), "UNIT"));
        }

        if (chartOfAccountRepository.count() == 0) {
            chartOfAccountRepository.save(new ChartOfAccount(null, "Aeroponic Tower Infrastructure", AccountType.ASSET));
            chartOfAccountRepository.save(new ChartOfAccount(null, "LED Lighting Assets", AccountType.ASSET));
            chartOfAccountRepository.save(new ChartOfAccount(null, "Cash/Bank", AccountType.ASSET));
            chartOfAccountRepository.save(new ChartOfAccount(null, "Accounts Receivable", AccountType.ASSET));

            chartOfAccountRepository.save(new ChartOfAccount(null, "Mineral Supplier Payables", AccountType.LIABILITY));
            chartOfAccountRepository.save(new ChartOfAccount(null, "Power Utility Creditors", AccountType.LIABILITY));

            chartOfAccountRepository.save(new ChartOfAccount(null, "Produce Sales Revenue", AccountType.INCOME));
            chartOfAccountRepository.save(new ChartOfAccount(null, "Farm Tour Revenue", AccountType.INCOME));

            chartOfAccountRepository.save(new ChartOfAccount(null, "LED Electricity Expenses", AccountType.EXPENSE));
            chartOfAccountRepository.save(new ChartOfAccount(null, "Nutrient Chemical Costs", AccountType.EXPENSE));
        }

        if (journalRepository.count() == 0) {
            journalRepository.save(new Journal(null, "Sales Journal", JournalType.SALES));
            journalRepository.save(new Journal(null, "Purchase Journal", JournalType.PURCHASE));
            journalRepository.save(new Journal(null, "Cash Journal", JournalType.CASH));
            journalRepository.save(new Journal(null, "Bank Journal", JournalType.BANK));
        }

        if (towerRepository.count() == 0) {
            Tower t1 = towerRepository.save(new Tower(null, "Aeroponic Module 1", 150, "Red/Blue 3:1"));
            Tower t2 = towerRepository.save(new Tower(null, "Aeroponic Module 2", 200, "Full Spectrum White"));
            Tower t3 = towerRepository.save(new Tower(null, "Aeroponic Module 3", 180, "Full Spectrum White"));

            telemetryRepository.save(new Telemetry(null, t1, 65.5, 950.0, LocalDateTime.now().minusHours(2), false));
            telemetryRepository.save(new Telemetry(null, t2, 54.0, 750.0, LocalDateTime.now().minusHours(1), true)); // trigger mist
            telemetryRepository.save(new Telemetry(null, t3, 72.0, 1050.0, LocalDateTime.now(), false));
        }

        if (analyticAccountRepository.count() == 0) {
            AnalyticAccount aa1 = analyticAccountRepository.save(new AnalyticAccount(null, "Aeroponic Module 1"));
            AnalyticAccount aa2 = analyticAccountRepository.save(new AnalyticAccount(null, "Aeroponic Module 2"));

            budgetRepository.save(new Budget(null, aa1, new BigDecimal("5000.00"), "2026-Q3"));
            budgetRepository.save(new Budget(null, aa2, new BigDecimal("7500.00"), "2026-Q3"));
        }
    }
}
