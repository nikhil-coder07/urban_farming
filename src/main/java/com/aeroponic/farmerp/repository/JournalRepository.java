package com.aeroponic.farmerp.repository;

import com.aeroponic.farmerp.model.Journal;
import com.aeroponic.farmerp.model.JournalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByType(JournalType type);
    Optional<Journal> findByName(String name);
}
