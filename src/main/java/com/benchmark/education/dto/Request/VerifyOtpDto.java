package com.benchmark.education.dto.Request;

import lombok.Data;

@Data
public class VerifyOtpDto {

    private int otp;
    private String otpToken;
}
