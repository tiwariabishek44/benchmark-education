package com.benchmark.education.controller.student;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.AddSalesDto;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.service.student.StudentBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/book")
public class StudentBookController {

    @Autowired
    private StudentBookService studentBookService;

    @GetMapping("/get-purchased-subjects")
    public ResponseDto<List<Subject>> getPurchasedSubjects(){
        return this.studentBookService.getPurchasedSubjects();
    }

    @PostMapping("/add-to-sales-ledger")
    public ResponseDto<String> addToSalesLedger(@RequestBody AddSalesDto dto){
       return this.studentBookService.addToSalesLedger(dto);
    }
}
