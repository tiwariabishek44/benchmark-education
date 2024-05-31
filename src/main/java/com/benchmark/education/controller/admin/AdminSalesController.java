package com.benchmark.education.controller.admin;

import com.benchmark.education.dto.Reponse.AccountResponseDto;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.TeacherManualResponsee;
import com.benchmark.education.dto.Reponse.TeacherSubjectResponse;
import com.benchmark.education.entity.*;
import com.benchmark.education.service.admin.AdminSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

//
//    @GetMapping("/api/open/teacher/manual/get/{email}")
//    public  List<TeacherManualResponsee> getManualAssignedToTeacher(@PathVariable("email") String email){
//
//        return this.adminSalesService.getManualAssignedToTeacher(email);
//    }
//
//
//
//    @PostMapping("/api/open/teacher/manual/add/{email}/{manual-id}")
//    public ResponseDto<String> addManualToTeacher(@PathVariable("email") String teacherEmail, @PathVariable("manual-id") int manualId){
//       return this.adminSalesService.addManualToTeacher(teacherEmail,manualId);
//    }
//
//    @PostMapping("/api/open/teacher/manual/delete/{id}")
//    public ResponseDto<String> unAssignManualToTeacher(@PathVariable("id") int id){
//     return this.adminSalesService.unAssignManualToTeacher(id);
//    }

    @PostMapping("/api/open/teacher/assign-subject/{subject-id}")
    public ResponseDto<String> assignSubjectToTeacher(@RequestBody HashMap<String, String> dto, @PathVariable("subject-id") int id){

     return  this.adminSalesService.assignSubjectToTeacher(dto.get("email"), id);
      }

    @PostMapping("/api/open/teacher/subject/delete/{subject-id}")
    public ResponseDto<String> unAssignSubjectToTeacher(@RequestBody HashMap<String, String> dto, @PathVariable("subject-id") int id){
       return this.adminSalesService.unAssignSubjectToTeacher(id, dto.get("email"));
    }

    @PostMapping("/api/open/teacher/subject/get/")
    public  List<TeacherSubjectResponse> getSubjectAssignedToTeacher(@RequestBody HashMap<String, String> data){
       return this.adminSalesService.getSubjectAssignedToTeacher(data.get("email"));
    }
}

