package com.benchmark.education.controller.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateSubjectDto;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.exception.GenericWrongRequestException;
import com.benchmark.education.service.admin.AdminBookService;
import com.benchmark.education.utils.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/book")
public class AdminBookController {

    @Autowired
    private AdminBookService adminBookService;



    @PostMapping("/create-subject")
    public ResponseDto<String> addSubject(@RequestBody CreateSubjectDto dto) {
      return this.adminBookService.addSubject(dto);
    }

    @DeleteMapping("/delete-subject/{id}")
    public ResponseDto<String> deleteSubject(@PathVariable("id") int id) throws IOException {
        return this.adminBookService.deleteBook(id);
    }

    @PostMapping("/change-subject-price/{id}/{price}")
    public ResponseDto<String> changePriceOfSubject(@PathVariable("id") int id, @PathVariable("price") String price){
      return this.adminBookService.changePriceOfSubject(id, price);
    }


    @PostMapping("/upload-book")
    public ResponseDto<String> uploadBook(@RequestParam("file") MultipartFile file ,
                                          @RequestParam(name ="subject-id", defaultValue = "0") String subjectID,
                                          @RequestParam("name") String name,
                                          @RequestParam(name = "price", defaultValue = "0") String price,
                                          @RequestParam(name="book-type") String bookType,
                                          @RequestParam(name="description", defaultValue = "") String description) throws IOException {
        return this.adminBookService.addBook(file, subjectID, name, price, bookType, description);

    }

    @DeleteMapping("/book/delete/{id}")
    public ResponseDto<String> deleteBook(@PathVariable("id") int id) throws IOException {
       return this.adminBookService.deleteBook(id);
    }

    @PostMapping("/upload-manual")
    public ResponseDto<String> uploadManual(@RequestParam("file") MultipartFile file ,
                                          @RequestParam("name") String name,
                                            @RequestParam("subjectId") int subjectId,
                                          @RequestParam(name="description", defaultValue = "") String description) throws IOException {
        return this.adminBookService.addManual(file, name, description, subjectId);

    }

    @DeleteMapping("/manual/delete/{id}")
    public ResponseDto<String> deleteManual(@PathVariable("id") int id) throws IOException {
        return this.adminBookService.deleteManual(id);
    }



}
