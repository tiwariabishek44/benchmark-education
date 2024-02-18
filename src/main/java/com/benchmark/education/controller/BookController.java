package com.benchmark.education.controller;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateSubjectDto;
import com.benchmark.education.entity.Book;
import com.benchmark.education.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

   private final BookService bookService;

   public BookController(BookService bookService) {
      this.bookService = bookService;
   }

   @PostMapping("/upload-book")
   public ResponseDto uploadBook(@RequestParam("file") MultipartFile file ,
                                 @RequestParam(name ="subject-id", defaultValue = "0") String subjectID,
                                 @RequestParam("name") String name,
                                 @RequestParam(name = "price", defaultValue = "0") String price,
                                 @RequestParam(name="book-type") String bookType,
                                 @RequestParam(name="description", defaultValue = "") String description) throws IOException {
      return this.bookService.addBook(file, subjectID, name, price, bookType, description);

   }

   @PostMapping("/addSubject")
   public ResponseDto addSubjet(@RequestBody() CreateSubjectDto dto) {
      return this.bookService.addSubjet(dto);
   }

   @DeleteMapping("/deleteSubject/{id}")
   public ResponseDto deleteSubject(@RequestParam("id") int id) {
      return this.bookService.deleteSubject(id);
   }

   @PatchMapping("/change-price-of-subject/{id}")
   public ResponseDto changePriceOfSubject(@RequestParam("id") int id,
                                           @RequestBody HashMap<String,String> request){
      return this.bookService.changePriceOfSubject(id, request.get("price"));
   }

   @GetMapping("/get-class-book")
   public List<Book> getClassBook(@RequestBody HashMap<String,String> request){
      return this.bookService.getClassBook(request.get("grade"), request.get("subject"),request.get("stream"));
   }

   @GetMapping("/get-mcq-books/all")
   public List<Book> getMCQBooks(){
      return this.bookService.getMCQBooks();
   }

   @GetMapping("/get-publication-books/all")
   public List<Book> getPublicationBooks(){
      return this.bookService.getPublicationBooks();
   }

   @DeleteMapping("/delete-book/{id}")
   public ResponseDto deleteBook(@RequestParam("id") int iad) {
      return this.bookService.deleteBook(id);
   }

}


