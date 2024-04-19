package com.benchmark.education.dto.Request;

import lombok.Data;

@Data
public class ForgetPassword2Dto {
    private String email;
    private String Otp;
    private String newPassWord;
}
