package com.benchmark.education.service.student;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.AddSalesDto;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.SalesLedgerRegister;
import com.benchmark.education.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentBookService {

    @Autowired
    SalesLedgerRegister salesLedgerRegister;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    SubjectRepository subjectRepository;



    public ResponseDto<List<Subject>> getPurchasedSubjects(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByStudentEmail(email);
        List<Integer> subjectList = salesLedgerList.stream().map(salesLedger -> salesLedger.getSubjectId()).collect(Collectors.toList());
        List<Subject> subjects = subjectRepository.findByIdIn(subjectList);
        return ResponseDto.Success(subjects,null);
    }

    public ResponseDto<String> addToSalesLedger( AddSalesDto dto){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByStudentEmailAndSubjectId(email,dto.getSubjectId());
        if(salesLedgerList.size()>0){
            return ResponseDto.Failure("", "You have already purchesed the course");
        }

        SalesLedger salesLedger = new SalesLedger();
        salesLedger.setStudentEmail(email);
        salesLedger.setSubjectId(dto.getSubjectId());
        this.salesLedgerRegister.save(salesLedger);
        return ResponseDto.Success("", "Successfully Purchased");
    }
}
