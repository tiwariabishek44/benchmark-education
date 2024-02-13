package com.benchmark.education.service;

import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.LoginSession;
import com.benchmark.education.mapper.AccountPojo;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.LoginSessionRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final LoginSessionRepository loginSessionRepository;

    public AccountService(AccountRepository accountRepository, LoginSessionRepository loginSessionRepository) {
        this.accountRepository = accountRepository;
        this.loginSessionRepository = loginSessionRepository;
    }

    public void createAccount(CreateAccountDto dto){
        Account exampleAccount = new Account();
        exampleAccount.setPhoneNumber(dto.getPhoneNumber());
        exampleAccount.setEmail(dto.getEmail());
        Example<Account> example = Example.of(exampleAccount);

        Account account = this.accountRepository.findBy( example, accountFetchableFluentQuery -> {
           return accountFetchableFluentQuery.firstValue();
        });
        if(account != null){
            //throw an exception that an account with same phonenumber exists.
        }

        account = AccountPojo.createAccountPojo(dto);
        this.accountRepository.save(account);

        // to create an active login session and save login hash to LoginSession table

        // return successful response

    }

    public void verifyTeacher(int id) throws Throwable {
        Account account = this.accountRepository.findById(id).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return null;
            }
        });

        account.setVerified(true);
        this.accountRepository.save(account);

        // return success response
    }

    public void studentFirstLoginAttempt(LoginCredentials loginCredentials) throws Throwable {
        Account accountExample = new Account();
        accountExample.setEmail(loginCredentials.getEmail());
        Account account = this.accountRepository.findBy(Example.of(accountExample), accountFetchQuery -> {
            return accountFetchQuery.firstValue();
        });
        if(account == null){
            // throw exception
        }

        /*
        this block checks if the password is correct
         */

        // return hasActiveLoginSession(account.getId());


    }

    public boolean hasActiveLoginSession(int id) throws Throwable {
        LoginSession loginSession = this.loginSessionRepository.findById(id).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return null;
            }
        });

        if(loginSession.getSessionHash()==null){
            return true;
        }

        return false;
    }

    public void doStudentLogin(LoginCredentials loginCredentials){
        Account accountExample = new Account();
        accountExample.setEmail(loginCredentials.getEmail());
        Account account = this.accountRepository.findBy(Example.of(accountExample), accountFetchQuery -> {
            return accountFetchQuery.firstValue();
        });
        if(account == null){
            // throw exception
        }

        /*
        compare the password
        generate new hash and save to database
         */

        /*
        generate access and refresh tokens
         */

        // return login hash along with access and refresh token
    }

}
