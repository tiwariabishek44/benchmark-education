package com.benchmark.education.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenExpiredException extends RuntimeException{
    private Object data;
    private String message;
}
