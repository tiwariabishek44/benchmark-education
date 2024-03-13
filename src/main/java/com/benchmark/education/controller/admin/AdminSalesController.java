package com.benchmark.education.controller.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.EcomerceEnquiry;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.service.admin.AdminSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/sales")
public class AdminSalesController {

    @Autowired
    private AdminSalesService adminSalesService;

    @GetMapping("students-with-purchase")
    public ResponseDto<List<Account>> getStudentsWithPurchase(){
        return this.adminSalesService.getStudentsWithPurchase();
    }

    @GetMapping("ecommerce/get-all-inquery")
    public ResponseDto<List<EcomerceEnquiry>> getAllEnquiry(){
       return this.adminSalesService.getAllEnquiry();
    }

    @GetMapping("/ecommerce/delete-inquiry/{id}")
    public ResponseDto<String> deleteEcommerceInquiry(@PathVariable int id){
        return this.adminSalesService.deleteEcommerceInquiry(id);
    }
}
