package com.benchmark.education.dto.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginCredentials {
    private String email;
    private String password;

}
