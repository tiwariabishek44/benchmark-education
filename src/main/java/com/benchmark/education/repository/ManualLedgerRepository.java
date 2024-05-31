package com.benchmark.education.repository;

import com.benchmark.education.entity.ManualLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualLedgerRepository extends JpaRepository<ManualLedger, Integer> {
    List<ManualLedger> findByEmailAndManualId(String email, int id);

    List<ManualLedger> findByEmail(String email);
}
