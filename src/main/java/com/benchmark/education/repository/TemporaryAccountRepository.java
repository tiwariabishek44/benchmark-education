package com.benchmark.education.repository;

import com.benchmark.education.entity.TemporaryAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryAccountRepository extends JpaRepository<TemporaryAccount, String> {
}
