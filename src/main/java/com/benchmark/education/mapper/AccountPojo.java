package com.benchmark.education.mapper;

import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.entity.Account;

public class AccountPojo {

    public static Account createAccountPojo(CreateAccountDto createAccountDto){
        Account account = new Account();
        account.setName(createAccountDto.getName());
        account.setEmail(createAccountDto.getEmail());
        account.setAccountType(createAccountDto.getAccountType());
        // to encrypt password using bcrypt
        account.setPassword(createAccountDto.getPassword());
        account.setPhoneNumber(createAccountDto.getPhoneNumber());
        account.setActive(true);
        if(Account.AccountType.STUDENT.equals(createAccountDto.getAccountType())){
            account.setVerified(true);
            account.setStream(createAccountDto.getStream());
        } else {
            account.setVerified(false);
        }
        return account;
    }
}
