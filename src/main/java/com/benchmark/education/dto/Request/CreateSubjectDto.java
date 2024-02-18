package com.benchmark.education.dto.Request;

import lombok.Data;

@Data
public class CreateSubjectDto {
    private String grade;
    private String subject;
    private String slug;
    private String stream;
    private String price;
}
