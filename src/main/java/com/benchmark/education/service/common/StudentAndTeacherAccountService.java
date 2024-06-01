package com.benchmark.education.service.common;

import com.benchmark.education.dto.Reponse.AccessTokenResponse;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.StudentAndTeacherLoginResponse;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.LoginSession;
import com.benchmark.education.exception.*;
import com.benchmark.education.mapper.AccountPojo;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.LoginSessionRepository;
import com.benchmark.education.repository.TemporaryAccountRepository;
import com.benchmark.education.utils.JavaMailUtil;
import com.benchmark.education.utils.JwtUtils;
import com.benchmark.education.utils.LoginUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndTeacherAccountService {

    private final AccountRepository accountRepository;
    private final LoginSessionRepository loginSessionRepository;
    private final AccountPojo accountPojo;
    private final PasswordEncoder passwordEncoder;
    private final LoginUtil loginUtil;
    private final JwtUtils jwtUtils;
    private final JavaMailUtil javaMailUtil;
    private final TemporaryAccountRepository temporaryAccountRepository;

    public StudentAndTeacherAccountService(AccountRepository accountRepository, LoginSessionRepository loginSessionRepository, AccountPojo accountPojo, PasswordEncoder passwordEncoder, LoginUtil loginUtil, JwtUtils jwtUtils, JavaMailUtil javaMailUtil, TemporaryAccountRepository temporaryAccountRepository) {
        this.accountRepository = accountRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.accountPojo = accountPojo;
        this.passwordEncoder = passwordEncoder;
        this.loginUtil = loginUtil;
        this.jwtUtils = jwtUtils;
        this.javaMailUtil = javaMailUtil;
        this.temporaryAccountRepository = temporaryAccountRepository;
    }



    public ResponseDto<String> firstLoginAttempt(LoginCredentials loginCredentials) throws Throwable {
        List<Account> accountList = this.accountRepository.findByEmail(loginCredentials.getEmail());
        if(accountList.size()==0){
            // throw exception
            throw new LoginException("No such account Exists");

        }

        Account account =accountList.get(0);
        if(Account.AccountType.ADMIN.equals(account.getAccountType())){
            throw new GenericWrongRequestException("Can't login using admin account");

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

    public ResponseDto<StudentAndTeacherLoginResponse> doLogin(LoginCredentials loginCredentials) throws Throwable {
        List<Account> accountList = this.accountRepository.findByEmail(loginCredentials.getEmail());
        if(accountList.size()==0){
            // throw exception
            throw new LoginException("No such account Exists");
        }

        Account account =accountList.get(0);
        if(!Account.AccountType.ADMIN.equals(account.getAccountType())){
            throw new GenericWrongRequestException("Can't login using an Admin email");
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
        Account.AccountType accountType = account.getAccountType();
        boolean isStudent = false;
        boolean isAccoundVerified = false;
        if(accountType.name().equals(Account.AccountType.STUDENT.name())){
            isStudent = true;
            isAccoundVerified = true;
        }else{
            isAccoundVerified = account.getIsVerified();
        }
        String accessToken = jwtUtils.getAccessToken(account.getEmail(), "account-type", accountType);
        String refreshToken = jwtUtils.getRefreshToken(account.getEmail(), "account-type", accountType);


        StudentAndTeacherLoginResponse loginResponse = new StudentAndTeacherLoginResponse(accessToken, refreshToken, loginSessionString, isStudent, isAccoundVerified);
        this.loginSessionRepository.save(loginSession);
        return   ResponseDto.Success(loginResponse, "Logged in successfully");


    }





}
