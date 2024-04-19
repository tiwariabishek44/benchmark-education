package com.benchmark.education.mapper;

import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.entity.Account;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountPojo {

    private final PasswordEncoder passwordEncoder;

    public AccountPojo(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public  Account createAccountPojo(CreateAccountDto createAccountDto){

        Account account = new Account();
        account.setName(createAccountDto.getName());
        account.setEmail(createAccountDto.getEmail());
        account.setAccountType(createAccountDto.getAccountType());
        // to encrypt password using bcrypt
        account.setPassword(createAccountDto.getPassword());
        account.setPhoneNumber(createAccountDto.getPhoneNumber());
        account.setIsActive(true);
        if(Account.AccountType.STUDENT.equals(createAccountDto.getAccountType())){
            account.setIsVerified(true);
            if(Account.Stream.SCIENCE.equals(createAccountDto.getStream())){
                account.setStream(Account.Stream.SCIENCE);
            } else{
                account.setStream(Account.Stream.MANAGEMENT);
            }

        } else {
            account.setIsVerified(false);
            account.setStream(null);
        }
        return account;
    }
}
