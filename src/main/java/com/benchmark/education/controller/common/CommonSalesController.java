package com.benchmark.education.controller.common;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.EcommerceInquiryDto;
import com.benchmark.education.service.common.SalesLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/open/common/sales")
public class CommonSalesController {

    @Autowired
    private SalesLedgerService salesLedgerService;

    @PostMapping("/add-inquiry")
    public ResponseDto<String> addEnquiry(@RequestBody EcommerceInquiryDto dto){
      return this.salesLedgerService.addEnquiry(dto);
    }
}
