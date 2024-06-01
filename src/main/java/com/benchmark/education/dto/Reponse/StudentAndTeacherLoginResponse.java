package com.benchmark.education.dto.Reponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentAndTeacherLoginResponse {
    private String accessToken;
    private String refreshToken;
    private String loginSessionHash;
    private boolean isStudent;
    private boolean isVerified;
}
