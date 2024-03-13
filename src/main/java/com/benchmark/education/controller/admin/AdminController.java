package com.benchmark.education.controller.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/service")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/verify-teacher/{id}")
    public ResponseDto<String> verifyTeacher(@PathVariable("id") int id){
        return this.adminService.verifyTeacher(id);
    }
}
