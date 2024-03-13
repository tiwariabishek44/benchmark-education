package com.benchmark.education.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoSuchAccountException extends RuntimeException{
    private String message;
}
