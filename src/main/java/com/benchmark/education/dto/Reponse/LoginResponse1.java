package com.benchmark.education.dto.Reponse;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse1 {
    private String accessToken;
    private String refreshToken;
    private String loginSessionHash;
    private boolean isVerified;
}
