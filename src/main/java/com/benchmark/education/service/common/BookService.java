package com.benchmark.education.service.common;

import com.benchmark.education.dto.Reponse.EcommerceBookDto;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateSubjectDto;
import com.benchmark.education.entity.*;
import com.benchmark.education.exception.GenericWrongRequestException;
import com.benchmark.education.repository.*;
import com.benchmark.education.utils.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    @Value("${protected-file-location}")
    private String protectedFileLocation;

    @Value("${public-file-location}")
    private String publicFIleLocation;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private  SubjectRepository subjectRepository;

    @Autowired
    private  BookRepository bookRepository;
    @Autowired
    private ManualRepository manualRepository;

    @Autowired
    private SalesLedgerRegister salesLedgerRegister;


    public ResponseDto<List<Subject>> getallSubject(){
        if(SecurityContextHolder.getContext()!=null
                && SecurityContextHolder.getContext().getAuthentication()!=null){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        if(email!=null) {
            List<Account> accountList = this.accountRepository.findByEmail(email);
            if (!accountList.isEmpty()) {
                if (accountList.get(0).getAccountType().name().equals(Account.AccountType.TEACHER.name())) {
                    List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByEmail(email);
                    List<Integer> subjectIdList = salesLedgerList.stream()
                            .map(salesLedger -> salesLedger.getSubjectId()).collect(Collectors.toList());
                    List<Subject> subjectList = this.subjectRepository.findByIdIn(subjectIdList);
                    return ResponseDto.Success(subjectList, "");

                }
            }

        }
        }

        List<Subject> subjectList = this.subjectRepository.findAll();
        return ResponseDto.Success(subjectList, null);
    }

    public ResponseDto<List<Book>> getClassBook(String grade, String subject, String stream){
        List<Subject> subjectList = this.subjectRepository.findByGradeAndSubjectAndStream(grade, subject, stream);
        if(subjectList.size()==0){
            return ResponseDto.Success(new ArrayList<>(), null);
        }

        List<Book> bookList = this.bookRepository.findBySubjectId(subjectList.get(0).getId());
        return ResponseDto.Success(bookList, null);
    }

    public ResponseDto<List<Book>> getMCQBooks(){
        List<Book> bookList = this.bookRepository.findByBookType(Book.BookType.MCQ);
        return ResponseDto.Success(bookList, null);
    }

    public ResponseDto<List<Book>> getPublicationBooks(){
        List<Book> bookList = this.bookRepository.findByBookType(Book.BookType.PUBLICATIONS);
        return ResponseDto.Success(bookList, null);
    }

    public ResponseDto<EcommerceBookDto> getEcommerceBook(int id){
        Optional<Book> bookOptional = this.bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new GenericWrongRequestException("no such book");
        }

        Book ecommerce = bookOptional.get();
        EcommerceBookDto dto = new EcommerceBookDto();
        dto.setName(ecommerce.getName());
        dto.setDescription(ecommerce.getDescription());
        dto.setPrice(ecommerce.getPrice());
        dto.setFileLocation(ecommerce.getFileLocation());
        return ResponseDto.Success(dto,"");
    }

    public ResponseDto<List<Manual>> getManualBySubject(int subjectId){
        List<Manual> manualList = this.manualRepository.findBySubjectId(subjectId);
        return ResponseDto.Success(manualList , "");
    }



}