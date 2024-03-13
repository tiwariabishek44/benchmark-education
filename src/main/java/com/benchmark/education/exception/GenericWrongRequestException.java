package com.benchmark.education.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericWrongRequestException extends RuntimeException{
    private String message;
}
