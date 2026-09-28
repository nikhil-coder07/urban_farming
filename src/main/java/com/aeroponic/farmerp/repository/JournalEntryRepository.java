package com.aeroponic.farmerp.repository;

import com.aeroponic.farmerp.model.AccountType;
import com.aeroponic.farmerp.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByAccountType(AccountType type);

    @Query("SELECT SUM(j.debit - j.credit) FROM JournalEntry j WHERE j.account.type = :type")
    BigDecimal getNetBalanceByAccountType(@Param("type") AccountType type);

    @Query("SELECT j.account.id, j.account.name, j.account.type, SUM(j.debit), SUM(j.credit) FROM JournalEntry j GROUP BY j.account.id, j.account.name, j.account.type")
    List<Object[]> getAccountBalances();

    @Query("SELECT SUM(j.debit - j.credit) FROM JournalEntry j WHERE j.analyticAccount.id = :analyticAccountId AND j.account.type = 'EXPENSE'")
    BigDecimal getActualExpenseByAnalyticAccount(@Param("analyticAccountId") Long analyticAccountId);
}
