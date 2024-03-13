package com.benchmark.education.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginException extends RuntimeException{


    private String message;
}
