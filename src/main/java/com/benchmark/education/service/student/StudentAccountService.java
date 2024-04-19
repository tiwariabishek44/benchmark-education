package com.benchmark.education.service.student;

import com.benchmark.education.dto.Reponse.LoginResponse;
import com.benchmark.education.dto.Reponse.OtpToken;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.AddSalesDto;
import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.dto.Request.VerifyOtpDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.LoginSession;
import com.benchmark.education.entity.TemporaryAccount;
import com.benchmark.education.exception.*;
import com.benchmark.education.mapper.AccountPojo;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.LoginSessionRepository;
import com.benchmark.education.repository.TemporaryAccountRepository;
import com.benchmark.education.utils.JavaMailUtil;
import com.benchmark.education.utils.JwtUtils;
import com.benchmark.education.utils.LoginUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAccountService {

    private final AccountRepository accountRepository;
    private final LoginSessionRepository loginSessionRepository;
    private final AccountPojo accountPojo;
    private final PasswordEncoder passwordEncoder;
    private final LoginUtil loginUtil;
    private final JwtUtils jwtUtils;
    private final JavaMailUtil javaMailUtil;
    private final TemporaryAccountRepository temporaryAccountRepository;

    public StudentAccountService(AccountRepository accountRepository, LoginSessionRepository loginSessionRepository, AccountPojo accountPojo, PasswordEncoder passwordEncoder, LoginUtil loginUtil, JwtUtils jwtUtils, JavaMailUtil javaMailUtil, TemporaryAccountRepository temporaryAccountRepository) {
        this.accountRepository = accountRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.accountPojo = accountPojo;
        this.passwordEncoder = passwordEncoder;
        this.loginUtil = loginUtil;
        this.jwtUtils = jwtUtils;
        this.javaMailUtil = javaMailUtil;
        this.temporaryAccountRepository = temporaryAccountRepository;
    }



    public ResponseDto<String> studentFirstLoginAttempt(LoginCredentials loginCredentials) throws Throwable {
        List<Account> accountList = this.accountRepository.findByEmail(loginCredentials.getEmail());
        if(accountList.size()==0){
            // throw exception
            throw new LoginException("No such account Exists");

        }

        Account account =accountList.get(0);
        if(!Account.AccountType.STUDENT.equals(account.getAccountType())){
            throw new GenericWrongRequestException("You can not login as a student");

        }
        String passwordHash = account.getPassword();
        System.out.println("password : " + loginCredentials.getPassword() +" : " +passwordHash);
        if(!passwordEncoder.matches(loginCredentials.getPassword(), passwordHash)){
            // throw exception that password provided is wrong
            throw new LoginException("Invalid Credential1");
        }


        return hasActiveLoginSession(account.getId());


    }

    public ResponseDto<String> hasActiveLoginSession(int id) throws Throwable {
        Optional<LoginSession> loginSessionOptional = this.loginSessionRepository.findById(id);
        LoginSession loginSession = null;

        if(!loginSessionOptional.isEmpty()){
            loginSession = loginSessionOptional.get();
        }

        if(loginSession == null){
            return ResponseDto.Success(null, "no active login session exists");
        }

        if(loginSession.getSessionHash()==null){
            return ResponseDto.Success("", "no active login session");
        }

        return ResponseDto.Failure("", "an active session exists");
    }

    public ResponseDto<LoginResponse> doStudentLogin(LoginCredentials loginCredentials) throws Throwable {
        List<Account> accountList = this.accountRepository.findByEmail(loginCredentials.getEmail());
        if(accountList.size()==0){
            // throw exception
            throw new LoginException("No such account Exists");
        }

        Account account =accountList.get(0);
        if(!Account.AccountType.STUDENT.equals(account.getAccountType())){
            throw new GenericWrongRequestException("You can not login as a student");
        }
        String passwordHash = account.getPassword();

        if(!passwordEncoder.matches(loginCredentials.getPassword(), passwordHash)){
            // throw exception that password provided is wrong
            throw new LoginException("Invalid Credential");
        }

        String loginSessionString = loginUtil.getLoginHash(account.getEmail());
        Optional<LoginSession> loginSessionOptional = this.loginSessionRepository.findById(account.getId());
        LoginSession loginSession = null;
        if(loginSessionOptional.isPresent()){
            loginSession = loginSessionOptional.get();
        }else{
            loginSession = new LoginSession();
            loginSession.setUserId(account.getId());
        }
        loginSession.setSessionHash(loginSessionString);
        Account.AccountType accountType = Account.AccountType.STUDENT;
        String accessToken = jwtUtils.getAccessToken(account.getEmail(), "account-type", accountType);
        String refreshToken = jwtUtils.getRefreshToken(account.getEmail(), "account-type", accountType);
        LoginResponse studentLoginResponse = new LoginResponse(accessToken, refreshToken, loginSessionString);
        this.loginSessionRepository.save(loginSession);
        System.out.println(StudentAccountService.class);
        System.out.println(studentLoginResponse.toString());
        System.out.println(StudentAccountService.class);
        return   ResponseDto.Success(studentLoginResponse, "Logged in successfully");


    }





}
