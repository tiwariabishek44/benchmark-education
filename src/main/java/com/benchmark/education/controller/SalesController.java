package com.benchmark.education.controller;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.AddSalesDto;
import com.benchmark.education.service.SalesLedgerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesLedgerService salesService;

    public SalesController(SalesLedgerService salesService) {
        this.salesService = salesService;

    }

    @PostMapping
    public ResponseDto addToSalesLedger(AddSalesDto dto){
        return this.salesService.addToSalesLedger(dto);
    }
}
