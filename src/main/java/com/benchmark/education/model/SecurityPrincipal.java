package com.benchmark.education.model;

import com.benchmark.education.entity.Account;
import lombok.Data;

@Data
public class SecurityPrincipal {
    private int id;
    private Account.AccountType accountType;
}
