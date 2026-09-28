package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private ChartOfAccount account;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal credit = BigDecimal.ZERO;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "ref_id", length = 50)
    private String refId;

    @ManyToOne
    @JoinColumn(name = "analytic_account_id")
    private AnalyticAccount analyticAccount;

    public JournalEntry() {}

    public JournalEntry(Long id, Journal journal, ChartOfAccount account, BigDecimal debit, BigDecimal credit, LocalDate entryDate, String refId, AnalyticAccount analyticAccount) {
        this.id = id;
        this.journal = journal;
        this.account = account;
        this.debit = debit != null ? debit : BigDecimal.ZERO;
        this.credit = credit != null ? credit : BigDecimal.ZERO;
        this.entryDate = entryDate != null ? entryDate : LocalDate.now();
        this.refId = refId;
        this.analyticAccount = analyticAccount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Journal getJournal() { return journal; }
    public void setJournal(Journal journal) { this.journal = journal; }
    public ChartOfAccount getAccount() { return account; }
    public void setAccount(ChartOfAccount account) { this.account = account; }
    public BigDecimal getDebit() { return debit; }
    public void setDebit(BigDecimal debit) { this.debit = debit; }
    public BigDecimal getCredit() { return credit; }
    public void setCredit(BigDecimal credit) { this.credit = credit; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public LocalDate getDate() { return entryDate; }
    public void setDate(LocalDate date) { this.entryDate = date; }
    public String getRefId() { return refId; }
    public void setRefId(String refId) { this.refId = refId; }
    public AnalyticAccount getAnalyticAccount() { return analyticAccount; }
    public void setAnalyticAccount(AnalyticAccount analyticAccount) { this.analyticAccount = analyticAccount; }
}
