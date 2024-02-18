package com.benchmark.education.service;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.AddSalesDto;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.repository.SalesLedgerRegister;

import java.util.List;

public class SalesLedgerService {

    private final SalesLedgerRegister salesLedgerRegister;

    public SalesLedgerService(SalesLedgerRegister salesLedgerRegister) {
        this.salesLedgerRegister = salesLedgerRegister;
    }

    public ResponseDto addToSalesLedger(AddSalesDto dto){
        List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByStudentIdAndSubjectId(dto.getStudentId(),dto.getSubjectId());
        if(salesLedgerList.size()>0){
            return ResponseDto.Failure(null, "You have already purchesed the course");
        }

        SalesLedger salesLedger = new SalesLedger();
        salesLedger.setStudentId(dto.getStudentId());
        salesLedger.setSubjectId(dto.getSubjectId());
        this.salesLedgerRegister.save(salesLedger);
        return ResponseDto.Success(null, "Successfully Purchased");
    }
}
