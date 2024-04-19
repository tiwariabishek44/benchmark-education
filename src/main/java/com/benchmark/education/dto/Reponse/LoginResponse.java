package com.benchmark.education.dto.Reponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String loginSessionHash;

}
