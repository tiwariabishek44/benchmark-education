package com.benchmark.education.service;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.dto.Request.VerifyOtpDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.LoginSession;
import com.benchmark.education.mapper.AccountPojo;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.LoginSessionRepository;
import com.benchmark.education.utils.JavaMailUtil;
import com.benchmark.education.utils.JwtUtils;
import com.benchmark.education.utils.LoginUtil;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final LoginSessionRepository loginSessionRepository;
    private final AccountPojo accountPojo;
    private final PasswordEncoder passwordEncoder;
    private final LoginUtil loginUtil;
    private final JwtUtils jwtUtils;
    private final JavaMailUtil javaMailUtil;

    public AccountService(AccountRepository accountRepository, LoginSessionRepository loginSessionRepository, AccountPojo accountPojo, PasswordEncoder passwordEncoder, LoginUtil loginUtil, JwtUtils jwtUtils, JavaMailUtil javaMailUtil) {
        this.accountRepository = accountRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.accountPojo = accountPojo;
        this.passwordEncoder = passwordEncoder;
        this.loginUtil = loginUtil;
        this.jwtUtils = jwtUtils;
        this.javaMailUtil = javaMailUtil;
    }

    public ResponseDto registrationPhase1(CreateAccountDto dto) throws MessagingException {
        List<Account> doesAccountExists = this.accountRepository.findByEmail(dto.getEmail());
        if(doesAccountExists.size()>0){
            //throw exception that account exists
        }

        int otp = javaMailUtil.sendOtp(dto.getEmail());
        String otpToken = this.jwtUtils.getOtpToken(Integer.toString(otp), "create-account-dto", dto);
        HashMap<String, String> result = new HashMap<>();
        result.put("otp-token",otpToken);
        return ResponseDto.Success(result, "Otp sent to email.");

    }

    public ResponseDto registrationPhase2(VerifyOtpDto dto){
        if(!this.jwtUtils.validateJwtToken(dto.getOtpToken())){
            // handle expired token
        }

        int otp = Integer.parseInt(
                this.jwtUtils.getUserNameFromJwtToken(dto.getOtpToken())
        );
        if(dto.getOtp() != otp){
            // handle wrong otp
        }

        CreateAccountDto createAccountDto = (CreateAccountDto) this.jwtUtils.getClaimFromJWTToken(dto.getOtpToken(),
                "create-account-dto");
        return createAccount(createAccountDto);
    }

    public ResponseDto createAccount(CreateAccountDto dto){
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

        account = accountPojo.createAccountPojo(dto);
        this.accountRepository.save(account);


        return ResponseDto.Success(null, null);

    }

    public ResponseDto verifyTeacher(int id) throws Throwable {
        Account account = this.accountRepository.findById(id).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return null;
            }
        });

        account.setVerified(true);
        this.accountRepository.save(account);

        return ResponseDto.Success(null,null);
    }

    public ResponseDto studentFirstLoginAttempt(LoginCredentials loginCredentials) throws Throwable {
        Account accountExample = new Account();
        accountExample.setEmail(loginCredentials.getEmail());
        Account account = this.accountRepository.findBy(Example.of(accountExample), accountFetchQuery -> {
            return accountFetchQuery.firstValue();
        });
        if(account == null){
            // throw exception
        }

        String passwordHash = passwordEncoder.encode(loginCredentials.getPassword());
        String passwordHashFromDatabase = account.getPassword();
        if(!passwordHashFromDatabase.equals(passwordHash)){
            // throw exception that password provided is wrong
        }


        return hasActiveLoginSession(account.getId());


    }

    public ResponseDto hasActiveLoginSession(int id) throws Throwable {
        Optional<LoginSession> loginSessionOptional = this.loginSessionRepository.findById(id);
        LoginSession loginSession = null;

        if(!loginSessionOptional.isEmpty()){
            loginSession = loginSessionOptional.get();
        }

        if(loginSession == null){
            return ResponseDto.Success(null, "no active login session exists");
        }

        if(loginSession.getSessionHash()==null){
            return ResponseDto.Success(null, "no active login session");
        }

        return ResponseDto.Failure(null, "an active session exists");
    }

    public ResponseDto doStudentLogin(LoginCredentials loginCredentials) throws Throwable {
        Account accountExample = new Account();
        accountExample.setEmail(loginCredentials.getEmail());
        Account account = this.accountRepository.findBy(Example.of(accountExample), accountFetchQuery -> accountFetchQuery.firstValue());
        if(account == null){
            // throw exception
        }

        String passwordHash = passwordEncoder.encode(loginCredentials.getPassword());
        String passwordHashFromDatabase = account.getPassword();
        if(!passwordHashFromDatabase.equals(passwordHash)){
            // throw exception that password provided is wrong
        }

        String loginSessionHash = loginUtil.getLoginHash(account.getEmail());
        Optional<LoginSession> loginSessionOptional = this.loginSessionRepository.findById(account.getId());
        LoginSession loginSession = null;
        if(loginSessionOptional.isPresent()){
            loginSession = loginSessionOptional.get();
        }else{
            loginSession = new LoginSession();
            loginSession.setUserId(account.getId());
        }
        loginSession.setSessionHash(loginSessionHash);
        Account.AccountType accountType = Account.AccountType.STUDENT;
        String accessToken = jwtUtils.getAccessToken(account.getEmail(), "account-type", accountType);
        String refreshToken = jwtUtils.getRefreshToken(account.getEmail(), "account-type", accountType);
        HashMap<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("loginSessionHash", loginSessionHash);
        this.loginSessionRepository.save(loginSession);
        return   ResponseDto.Success(result, "Logged in successfully");

    }

    public ResponseDto generalLogin(LoginCredentials loginCredentials){
        List<Account> accountList = this.accountRepository.findByEmail(loginCredentials.getEmail());
        if(accountList.size()==0){
            // throw exception that there is no account for the provided email address
        }

        Account account = accountList.get(0);

        if(account.getAccountType().equals(Account.AccountType.STUDENT)){
            // throw exception for using this api by student;
        }
        String passwordHashFromDatabase = account.getPassword();
        String passwordHash = passwordEncoder.encode(loginCredentials.getPassword());
        if(!passwordHashFromDatabase.equals(passwordHash)){
            //throw exception that password does not match
        }
        Account.AccountType accountType;
        if(Account.AccountType.TEACHER.equals(account.getAccountType())){
            accountType = Account.AccountType.TEACHER;
        } else{
            accountType = Account.AccountType.ADMIN;
        }
        String accessToken = jwtUtils.getAccessToken(account.getEmail(), "account-type", accountType);
        String refreshToken = jwtUtils.getRefreshToken(account.getEmail(), "account-type", accountType);

        HashMap<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
       return ResponseDto.Success(result, "Successfully Logged In");
    }

    public ResponseDto getAccessTokenFromRefreshToken(String refreshToken){
        boolean isTokenValid = this.jwtUtils.validateJwtToken(refreshToken);
        if(!isTokenValid){
            // handle invalid token. send redirection command for login
        }
        String username = this.jwtUtils.getUserNameFromJwtToken(refreshToken);
        Account.AccountType accountType = (Account.AccountType) this.jwtUtils.getClaimFromJWTToken(refreshToken,"account-type");
        String accessToken = this.jwtUtils.getAccessToken(username, "access-token", accountType);
        HashMap<String, String> result =  new HashMap<>();
        result.put("accessToken", accessToken);
        return ResponseDto.Success(result, null);
    }

    public void getStudentsAlongWithSubjectsBought(){

    }

}
