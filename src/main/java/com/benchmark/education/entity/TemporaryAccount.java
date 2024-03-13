package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class TemporaryAccount {

    @Id
    private String email;
    private String phoneNumber;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Account.AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Account.Stream stream ;
}
