package com.aeroponic.farmerp.repository;

import com.aeroponic.farmerp.model.AccountType;
import com.aeroponic.farmerp.model.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long> {
    Optional<ChartOfAccount> findByName(String name);
    List<ChartOfAccount> findByType(AccountType type);
}
