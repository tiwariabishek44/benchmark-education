package com.benchmark.education.repository;

import com.benchmark.education.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByEmail(String email);

    List<Account> findByEmailAndAccountType(String email, Account.AccountType admin);

    List<Account> findByEmailIn(List<String> studentEmails);
}
