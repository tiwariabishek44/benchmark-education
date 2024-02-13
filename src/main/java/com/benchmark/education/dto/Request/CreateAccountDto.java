package com.benchmark.education.dto.Request;

import com.benchmark.education.entity.Account;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateAccountDto {

    private String email;
    private String phoneNumber;
    private String name;
    private String password;
    private Account.AccountType accountType;
    private Account.Stream stream;
    private LocalDate createdDate;
    private boolean isVerified;

}
