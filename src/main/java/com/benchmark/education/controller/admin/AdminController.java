package com.benchmark.education.controller.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.AccountResponseDto;
import com.benchmark.education.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/service")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/verify-teacher/{id}")
    public ResponseDto<String> verifyTeacher(@PathVariable("id") int id){
        return this.adminService.verifyTeacher(id);
    }

    @GetMapping("/get-teacher/unverified")
    public ResponseDto<List<AccountResponseDto>> getUnverifiedTeacher(){
       return this.adminService.getUnverifiedTeacher();
    }

    @GetMapping("/get-teacher/verified")
    public ResponseDto<List<AccountResponseDto>> getVerifiedTeacher() {
        return this.adminService.getVerifiedTeacher();
    }
}
