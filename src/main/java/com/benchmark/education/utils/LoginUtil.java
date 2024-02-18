package com.benchmark.education.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoginUtil {

    private final PasswordEncoder passwordEncoder;

    public LoginUtil(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String getLoginHash(String username){

        String loginText = username+ "_" + LocalDate.now().toString();
        return passwordEncoder.encode(username);
    }
}
