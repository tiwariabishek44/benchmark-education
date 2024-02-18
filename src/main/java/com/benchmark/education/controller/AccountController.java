package com.benchmark.education.controller;


import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.dto.Request.VerifyOtpDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.service.AccountService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/registrationPhase1")
    public ResponseDto registrationPhase1(@RequestBody() CreateAccountDto dto) throws MessagingException {
       return this.accountService.registrationPhase1(dto);

    }

    @PostMapping("/registrationPhase2")
    public ResponseDto registrationPhase2(VerifyOtpDto dto){
      return this.accountService.registrationPhase2(dto);
    }

    @PatchMapping("/verifyTeacher/{id}")
    public ResponseDto verifyTeacher(@RequestParam("id") int id) throws Throwable {
        return this.accountService.verifyTeacher(id);
    }

    @PostMapping("/studentFirstLoginAttempt")
    public ResponseDto studentFirstLoginAttempt(LoginCredentials loginCredentials) throws Throwable {
        return this.accountService.studentFirstLoginAttempt(loginCredentials);
    }

    @PostMapping("/doStudentLogin")
    public ResponseDto doStudentLogin(LoginCredentials loginCredentials) throws Throwable {
        return this.accountService.doStudentLogin(loginCredentials);
    }

    @PostMapping("/generalLogin")
    public ResponseDto generalLogin(LoginCredentials loginCredentials){
        return this.accountService.generalLogin(loginCredentials);
    }

    @PostMapping("/getAccessTokenFromRefreshToken")
    public ResponseDto getAccessTokenFromRefreshToken(String refreshToken){
        return this.accountService.getAccessTokenFromRefreshToken(refreshToken);
    }

}
