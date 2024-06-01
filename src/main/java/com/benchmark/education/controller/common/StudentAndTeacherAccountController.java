package com.benchmark.education.controller.common;

import com.benchmark.education.dto.Reponse.AccessTokenResponse;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.StudentAndTeacherLoginResponse;
import com.benchmark.education.dto.Request.LoginCredentials;
import com.benchmark.education.service.common.StudentAndTeacherAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/open/student-and-teacher")
public class StudentAndTeacherAccountController {

    @Autowired
    private StudentAndTeacherAccountService studentAndTeacherAccountService;



    @PostMapping("/first-login-attempt")
    public ResponseDto<String> firstLoginAttempt(@RequestBody LoginCredentials loginCredentials) throws Throwable {
        return this.studentAndTeacherAccountService.firstLoginAttempt(loginCredentials);
    }

    @PostMapping("/login")
    public ResponseDto<StudentAndTeacherLoginResponse> doLogin(@RequestBody LoginCredentials loginCredentials) throws Throwable {
        return this.studentAndTeacherAccountService.doLogin(loginCredentials);
    }
}
