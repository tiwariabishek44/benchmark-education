package com.benchmark.education.controller.student;

import com.benchmark.education.dto.Reponse.LoginResponse;
import com.benchmark.education.dto.Reponse.OtpToken;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateAccountDto;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.dto.Request.VerifyOtpDto;
import com.benchmark.education.service.student.StudentAccountService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/open/student")
public class StudentAccountController {

    @Autowired
    private StudentAccountService studentAccountService;



    @PostMapping("/student-first-login-attempt")
    public ResponseDto<String> studentFirstLoginAttempt(@RequestBody LoginCredentials loginCredentials) throws Throwable {
        return this.studentAccountService.studentFirstLoginAttempt(loginCredentials);
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> doStudentLogin(@RequestBody LoginCredentials loginCredentials) throws Throwable {
        return this.studentAccountService.doStudentLogin(loginCredentials);
    }
}
