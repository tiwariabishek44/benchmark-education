package com.benchmark.education.controller.common;

import com.benchmark.education.dto.Reponse.AccessTokenResponse;
import com.benchmark.education.dto.Reponse.LoginResponse1;
import com.benchmark.education.dto.Reponse.OtpToken;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.*;
import com.benchmark.education.service.common.AccountService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/open/common/account")
public class CommonAccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/registration-phase-1")
    public ResponseDto<OtpToken> registrationPhase1(@RequestBody CreateAccountDto dto) throws MessagingException {
        return this.accountService.registrationPhase1(dto);
    }

    @PostMapping("/registration-phase-2")
    public ResponseDto<String> registrationPhase2(@RequestBody VerifyOtpDto dto) {
        return this.accountService.registrationPhase2(dto);
    }

    @PostMapping("/common-login")
    public ResponseDto<LoginResponse1> generalLogin(@RequestBody LoginCredentials loginCredentials){
        return this.accountService.generalLogin(loginCredentials);
    }

    @PostMapping("/access-token/refresh")
    public ResponseDto<AccessTokenResponse> getAccessTokenFromRefreshToken(@RequestBody RefreshAccessTokenDto dto)
    {
        return this.accountService.getAccessTokenFromRefreshToken(dto.getRefreshToken());
    }

    @PostMapping("/forget-password/step1")
    public ResponseDto<String> forgetPassword1(@RequestBody ForgetPassword1Dto dto) throws MessagingException {
        return this.accountService.forgetPassword1(dto);
    }

    @PostMapping("/forget-password/step2")
    public ResponseDto<String> forgetPassword2(@RequestBody ForgetPassword2Dto dto) throws MessagingException {
        return this.accountService.forgetPassword2(dto);
    }

}
