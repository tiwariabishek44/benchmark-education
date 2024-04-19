package com.benchmark.education.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EcommerceInquiryDto
{
    private int bookId;
    private String name;
    private String phoneNumber;
    private String message;
}
