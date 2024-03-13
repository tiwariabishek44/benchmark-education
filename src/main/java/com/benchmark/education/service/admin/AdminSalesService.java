package com.benchmark.education.service.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.EcomerceEnquiry;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.EcommerceInquiryRepository;
import com.benchmark.education.repository.SalesLedgerRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminSalesService {



    @Autowired
    private EcommerceInquiryRepository ecommerceInquiryRepository;

    @Autowired
    private SalesLedgerRegister salesLedgerRegister;

    @Autowired
    private AccountRepository accountRepository;


    public ResponseDto<List<Account>> getStudentsWithPurchase(){
        List<SalesLedger>  salesLedgerList = this.salesLedgerRegister.findAll();
        List<String> studentEmails = salesLedgerList.stream().map(salesLedger -> salesLedger.getStudentEmail()).collect(Collectors.toList());
        List<Account> studentAccounts = this.accountRepository.findByEmailIn(studentEmails);
        studentAccounts = studentAccounts.stream().map(studentAccount -> {
            studentAccount.setPassword(null);
            return studentAccount;
        }).collect(Collectors.toList());
        return ResponseDto.Success(studentAccounts, null);
    }

    public ResponseDto<List<EcomerceEnquiry>> getAllEnquiry(){
        List<EcomerceEnquiry> ecomerceEnquiryList = this.ecommerceInquiryRepository.findAll();
        ecomerceEnquiryList = ecomerceEnquiryList.stream().map(ecomerceEnquiry -> {
            ecomerceEnquiry.getAccount().setPassword(null);
            return ecomerceEnquiry;
        }). collect(Collectors.toList());
        return ResponseDto.Success(ecomerceEnquiryList, null);
    }

    public ResponseDto<String> deleteEcommerceInquiry(int id){
        this.ecommerceInquiryRepository.deleteById(id);
        return ResponseDto.Success("", null);
    }
}
