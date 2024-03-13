package com.benchmark.education.service.common;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.AddSalesDto;
import com.benchmark.education.dto.Request.EcommerceInquiryDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.EcomerceEnquiry;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.EcommerceInquiryRepository;
import com.benchmark.education.repository.SalesLedgerRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalesLedgerService {

    @Autowired
    private SalesLedgerRegister salesLedgerRegister;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EcommerceInquiryRepository ecommerceInquiryRepository;

    public ResponseDto<String> addEnquiry( EcommerceInquiryDto dto){
        Book book = this.bookRepository.findById(dto.getBookId()).orElse(null);
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Account> accountList = this.accountRepository.findByEmail(email);
        EcomerceEnquiry ecomerceEnquiry = new EcomerceEnquiry();
        ecomerceEnquiry.setMessage(dto.getMessage());
        ecomerceEnquiry.setBook(book);
        ecomerceEnquiry.setAccount(accountList.get(0));
        this.ecommerceInquiryRepository.save(ecomerceEnquiry);
        return ResponseDto.Success("", null);

    }


}
