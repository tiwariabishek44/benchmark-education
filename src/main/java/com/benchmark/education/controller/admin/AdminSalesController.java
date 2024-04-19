package com.benchmark.education.controller.admin;

import com.benchmark.education.dto.Reponse.AccountResponseDto;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.EcomerceEnquiry;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.service.admin.AdminSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class AdminSalesController {

    @Autowired
    private AdminSalesService adminSalesService;

    @GetMapping("/api/admin/sales/students-with-purchase")
    public ResponseDto<List<AccountResponseDto>> getStudentsWithPurchase(){
        return this.adminSalesService.getStudentsWithPurchase();
    }

    @GetMapping("/api/open/sales/ecommerce/get-all-inquiry")
    public ResponseDto<List<EcomerceEnquiry>> getAllEnquiry(){
       return this.adminSalesService.getAllEnquiry();
    }

    @DeleteMapping("/api/admin/sales/ecommerce/delete-inquiry/{id}")
    public ResponseDto<String> deleteEcommerceInquiry(@PathVariable int id){
        return this.adminSalesService.deleteEcommerceInquiry(id);
    }
}

