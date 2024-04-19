package com.benchmark.education.dto.Reponse;

import com.benchmark.education.entity.Account;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder

public class AccountResponseDto {

int id;
    private String email;


    private String phoneNumber;

    private String name;


    private String password;



    private Account.AccountType accountType;


    private Account.Stream stream;


    private LocalDate createdDate;

    private Boolean isActive;

    private Boolean isVerified;



}
