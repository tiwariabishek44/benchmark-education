package com.benchmark.education.controller.common;


import com.benchmark.education.dto.Reponse.EcommerceBookDto;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.ClassBookDto;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.service.common.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/open/common/book")
public class CommonBookController {

    @Autowired
    private BookService bookService;


    @GetMapping("/subject/get-all")
    public ResponseDto<List<Subject>> getall(){
        return this.bookService.getallSubject();
    }

    @PostMapping("/class-books/get-all")
    public ResponseDto<List<Book>> getClassBook(@RequestBody ClassBookDto request){
        return this.bookService.getClassBook(request.getGrade(), request.getSubject(),request.getStream());
    }

    @GetMapping("/mcq/get-all")
    public ResponseDto<List<Book>> getMCQBooks(){
      return this.bookService.getMCQBooks();
    }

    @GetMapping("/ecommerce/get-all")
    public ResponseDto<List<Book>> getPublicationBooks(){
        return this.bookService.getPublicationBooks();
    }

    @GetMapping("/ecommerce/get/{id}")
    public ResponseDto<EcommerceBookDto> getEcommerceBook(@PathVariable("id") int id){
       return this.bookService.getEcommerceBook(id);
    }
}
