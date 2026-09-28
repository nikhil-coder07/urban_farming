package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.model.*;
import com.aeroponic.farmerp.repository.ChartOfAccountRepository;
import com.aeroponic.farmerp.repository.JournalEntryRepository;
import com.aeroponic.farmerp.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LedgerService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Transactional
    public void postBalancedEntry(ChartOfAccount debitAccount, ChartOfAccount creditAccount, BigDecimal amount, LocalDate date, String refId, AnalyticAccount analyticAccount, JournalType journalType) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Entry amount must be greater than zero");
        }

        Journal journal = journalRepository.findByType(journalType)
                .orElseGet(() -> {
                    Journal j = new Journal();
                    j.setName(journalType.name() + " Journal");
                    j.setType(journalType);
                    return journalRepository.save(j);
                });

        LocalDate entryDate = date != null ? date : LocalDate.now();

        // Debit Entry
        JournalEntry debitEntry = new JournalEntry();
        debitEntry.setJournal(journal);
        debitEntry.setAccount(debitAccount);
        debitEntry.setDebit(amount);
        debitEntry.setCredit(BigDecimal.ZERO);
        debitEntry.setDate(entryDate);
        debitEntry.setRefId(refId);
        debitEntry.setAnalyticAccount(analyticAccount);
        journalEntryRepository.save(debitEntry);

        // Credit Entry
        JournalEntry creditEntry = new JournalEntry();
        creditEntry.setJournal(journal);
        creditEntry.setAccount(creditAccount);
        creditEntry.setDebit(BigDecimal.ZERO);
        creditEntry.setCredit(amount);
        creditEntry.setDate(entryDate);
        creditEntry.setRefId(refId);
        creditEntry.setAnalyticAccount(analyticAccount);
        journalEntryRepository.save(creditEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }
}
