package com.benchmark.education.dto.Reponse;

import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.EcomerceEnquiry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EcommerceEnquiryGetAllDto {

    private Account account;
    private EcomerceEnquiry ecomerceEnquiry;
}
