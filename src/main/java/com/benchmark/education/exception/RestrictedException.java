package com.benchmark.education.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedException extends  RuntimeException{

    private String message;
}
