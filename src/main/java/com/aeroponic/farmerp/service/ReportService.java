package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.*;
import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.*;
import com.aeroponic.farmerp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private AnalyticAccountRepository analyticAccountRepository;

    public BalanceSheetReportDTO getBalanceSheet() {
        List<ChartOfAccount> allAccounts = chartOfAccountRepository.findAll();
        List<JournalEntry> entries = journalEntryRepository.findAll();

        List<AccountBalanceDTO> assets = new ArrayList<>();
        BigDecimal totalAssets = BigDecimal.ZERO;

        List<AccountBalanceDTO> liabilities = new ArrayList<>();
        BigDecimal totalLiabilities = BigDecimal.ZERO;

        for (ChartOfAccount acc : allAccounts) {
            if (acc.getType() == AccountType.ASSET) {
                BigDecimal debitSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal creditSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal balance = debitSum.subtract(creditSum);
                assets.add(new AccountBalanceDTO(acc.getId(), acc.getName(), acc.getType(), balance));
                totalAssets = totalAssets.add(balance);
            } else if (acc.getType() == AccountType.LIABILITY) {
                BigDecimal debitSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal creditSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal balance = creditSum.subtract(debitSum);
                liabilities.add(new AccountBalanceDTO(acc.getId(), acc.getName(), acc.getType(), balance));
                totalLiabilities = totalLiabilities.add(balance);
            }
        }

        BigDecimal netPosition = totalAssets.subtract(totalLiabilities);
        return new BalanceSheetReportDTO(assets, totalAssets, liabilities, totalLiabilities, netPosition);
    }

    public ProfitLossReportDTO getProfitAndLoss() {
        List<ChartOfAccount> allAccounts = chartOfAccountRepository.findAll();
        List<JournalEntry> entries = journalEntryRepository.findAll();

        List<AccountBalanceDTO> income = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;

        List<AccountBalanceDTO> expenses = new ArrayList<>();
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (ChartOfAccount acc : allAccounts) {
            if (acc.getType() == AccountType.INCOME) {
                BigDecimal debitSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal creditSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal balance = creditSum.subtract(debitSum);
                income.add(new AccountBalanceDTO(acc.getId(), acc.getName(), acc.getType(), balance));
                totalIncome = totalIncome.add(balance);
            } else if (acc.getType() == AccountType.EXPENSE) {
                BigDecimal debitSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal creditSum = entries.stream()
                        .filter(e -> e.getAccount().getId().equals(acc.getId()))
                        .map(JournalEntry::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal balance = debitSum.subtract(creditSum);
                expenses.add(new AccountBalanceDTO(acc.getId(), acc.getName(), acc.getType(), balance));
                totalExpenses = totalExpenses.add(balance);
            }
        }

        BigDecimal netProfit = totalIncome.subtract(totalExpenses);
        return new ProfitLossReportDTO(income, totalIncome, expenses, totalExpenses, netProfit);
    }

    public BudgetReportDTO getBudgetReport(Long analyticAccountId) {
        AnalyticAccount aa = analyticAccountRepository.findById(analyticAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Analytic Account not found with ID: " + analyticAccountId));

        List<Budget> budgets = budgetRepository.findByAnalyticAccountId(analyticAccountId);
        BigDecimal allocated = budgets.stream()
                .map(Budget::getAllocatedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String period = budgets.isEmpty() ? "N/A" : budgets.get(0).getPeriod();

        BigDecimal actual = journalEntryRepository.getActualExpenseByAnalyticAccount(analyticAccountId);
        if (actual == null) {
            actual = BigDecimal.ZERO;
        }

        BigDecimal variance = allocated.subtract(actual);
        Double pct = allocated.compareTo(BigDecimal.ZERO) > 0 ?
                actual.divide(allocated, 4, RoundingMode.HALF_UP).doubleValue() * 100 : 0.0;

        return new BudgetReportDTO(aa.getId(), aa.getZoneName(), period, allocated, actual, variance, pct);
    }
}
