package com.benchmark.education.service.common;

import com.benchmark.education.dto.Reponse.OtpToken;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.LoginResponse;
import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.dto.Request.VerifyOtpDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.TemporaryAccount;
import com.benchmark.education.exception.*;
import com.benchmark.education.mapper.AccountPojo;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.TemporaryAccountRepository;
import com.benchmark.education.utils.JavaMailUtil;
import com.benchmark.education.utils.JwtUtils;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TemporaryAccountRepository temporaryAccountRepository;
    private final AccountPojo accountPojo;
    private final JavaMailUtil javaMailUtil;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, TemporaryAccountRepository temporaryAccountRepository, AccountPojo accountPojo, JavaMailUtil javaMailUtil) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.temporaryAccountRepository = temporaryAccountRepository;
        this.accountPojo = accountPojo;
        this.javaMailUtil = javaMailUtil;
    }


    public ResponseDto<OtpToken> registrationPhase1(CreateAccountDto dto) throws MessagingException {
        List<Account> doesAccountExists = this.accountRepository.findByEmail(dto.getEmail());
        if(doesAccountExists.size()>0){
            //throw exception that account exists
            throw new AccountCreationException("An account with provided email address already exists");
        }

        if(Account.AccountType.ADMIN.name().equals(dto.getAccountType().name())){
            throw new GenericWrongRequestException("You cannot create Admin Account");
        }

        int otp = javaMailUtil.sendOtp(dto.getEmail());
        String otpToken = this.jwtUtils.getOtpToken(Integer.toString(otp),dto.getEmail());
        TemporaryAccount temporaryAccount = new TemporaryAccount();
        Optional<TemporaryAccount> temporaryAccountOptional = this.temporaryAccountRepository.findById(dto.getEmail());
        if(temporaryAccountOptional.isEmpty()){
            temporaryAccount.setEmail(dto.getEmail());
        }else{
            temporaryAccount = temporaryAccountOptional.get();
        }
        temporaryAccount.setName(dto.getName());
        temporaryAccount.setPhoneNumber(dto.getPhoneNumber());
        temporaryAccount.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        temporaryAccount.setAccountType(dto.getAccountType());
        temporaryAccount.setStream(dto.getStream());
        this.temporaryAccountRepository.save(temporaryAccount);
        OtpToken token = new OtpToken(otpToken);


        //todo

        return ResponseDto.Success(token, "Otp sent to email.");

    }


    public ResponseDto<String> registrationPhase2(VerifyOtpDto dto){
        if(!this.jwtUtils.validateJwtToken(dto.getOtpToken())){
            // handle expired token
            throw new TokenExpiredException(null,"The token is expired");
        }
        String principal =   this.jwtUtils.getUserNameFromJwtToken(dto.getOtpToken());

        int otp = Integer.parseInt(principal.substring(0,4)

        );
        if(dto.getOtp() != otp){
            // handle wrong otp
            throw new WrongOtpException(null,"Otp provided is incorrect");
        }

        String email = principal.substring(5);
        Optional<TemporaryAccount> temporaryAccountOptional = this.temporaryAccountRepository.findById(email);
        TemporaryAccount temporaryAccount = temporaryAccountOptional.get();
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setName(temporaryAccount.getName());
        createAccountDto.setEmail(temporaryAccount.getEmail());
        createAccountDto.setPassword(temporaryAccount.getPassword());
        createAccountDto.setPhoneNumber(temporaryAccount.getPhoneNumber());
        createAccountDto.setAccountType(temporaryAccount.getAccountType());
        createAccountDto.setStream(temporaryAccount.getStream());
        return createAccount(createAccountDto);
    }

    public ResponseDto<String> createAccount(CreateAccountDto dto){


        List<Account> accountList = this.accountRepository.findByEmail(dto.getEmail());
        if(accountList.size()>0){
            //throw an exception that an account with same phonenumber exists.
            throw new AccountCreationException("An account with given Email already exits");
        }

        Account account = accountPojo.createAccountPojo(dto);
        System.out.print("account test" + account.toString());
        this.accountRepository.save(account);


        return ResponseDto.Success("", null);

    }

    public ResponseDto<LoginResponse> generalLogin(LoginCredentials loginCredentials){
        List<Account> accountList = this.accountRepository.findByEmail(loginCredentials.getEmail());
        if(accountList.size()==0){
            // throw exception that there is no account for the provided email address
            throw new LoginException("No such account Exists");
        }

        Account account = accountList.get(0);

        if(account.getAccountType().equals(Account.AccountType.STUDENT)){
            // throw exception for using this api by student;
            throw new RestrictedException("You cannot use student email to login");
        }
        String passwordHashFromDatabase = account.getPassword();
        String passwordHash = passwordEncoder.encode(loginCredentials.getPassword());

        if(!passwordEncoder.matches(loginCredentials.getPassword(), account.getPassword())){
            //throw exception that password does not match
            throw new LoginException("You provided a wrong Account Credentials. Try again!");
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
        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken, null);

        //todo

       return ResponseDto.Success(loginResponse, "Successfully Logged In");
    }

    public ResponseDto<LoginResponse> getAccessTokenFromRefreshToken(String refreshToken){
        boolean isTokenValid = this.jwtUtils.validateJwtToken(refreshToken);
        if(!isTokenValid){
            // handle invalid token. send redirection command for login
            throw new WrongRefreshToken();
        }
        String username = this.jwtUtils.getUserNameFromJwtToken(refreshToken);
        Account.AccountType accountType = (Account.AccountType) this.jwtUtils.getClaimFromJWTToken(refreshToken,"account-type");
        String accessToken = this.jwtUtils.getAccessToken(username, "access-token", accountType);
        HashMap<String, String> result =  new HashMap<>();
        result.put("accessToken", accessToken);
        LoginResponse loginResponse = new LoginResponse(accessToken, null, null);

        //todo

        return ResponseDto.Success(loginResponse, null);
    }

}
