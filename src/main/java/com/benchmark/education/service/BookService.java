package com.benchmark.education.service;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateSubjectDto;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.SubjectRepository;
import com.benchmark.education.utils.BookUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Value("${file-upload-location}")
    private String fileUploadLocation;

    private final SubjectRepository subjectRepository;
    private final BookRepository bookRepository;

    public BookService(SubjectRepository subjectRepository, BookRepository bookRepository) {
        this.subjectRepository = subjectRepository;
        this.bookRepository = bookRepository;
    }

    public ResponseDto addSubjet(CreateSubjectDto dto) {
        Subject bookRegister = new Subject();
        bookRegister.setGrade(dto.getGrade());
        bookRegister.setSubject(dto.getSubject());
        bookRegister.setSlug(dto.getSlug());
        bookRegister.setPrice(dto.getPrice());
        this.subjectRepository.save(bookRegister);
        return ResponseDto.Success(null, null);
    }

    public ResponseDto deleteSubject(int id) {
        this.subjectRepository.deleteById(id);
        return ResponseDto.Success(null, null);

    }

    public ResponseDto changePriceOfSubject(int id, String price){
        Optional<Subject> subjectOptional = this.subjectRepository.findById(id);
        if(subjectOptional.isEmpty()){
            // handle wrong id
        }

        Subject subject = subjectOptional.get();
        subject.setPrice(price);
        this.subjectRepository.save(subject);
        return ResponseDto.Success(null, null);
    }

    public List<Subject> getall(){
        List<Subject> subjectList = this.subjectRepository.findAll();
        return subjectList;
    }

    // service logic to add book
    public ResponseDto addBook(MultipartFile file,
                        String subjectId1,
                        String name,
                        String price,
                        String bookType1,
                        String description) throws IOException {
        Book.BookType bookType = BookUtils.getBookType(bookType1);
        Book book = new Book();
        if (Book.BookType.PUBLICATIONS.equals(bookType) || Book.BookType.MCQ.equals(bookType)) {
            book.setName(name);
            book.setDescription(description);
            book.setBookType(bookType);
            book.setPrice(price);

        } else {
            int subjectId = Integer.parseInt(subjectId1);
            book.setSubjectId(subjectId);
            book.setName(name);
            book.setDescription(description);
            book.setBookType(bookType);
        }

        String fileLocation = uploadFile(file, bookType);
        book.setFileLocation(fileLocation);
        this.bookRepository.save(book);
        return ResponseDto.Success(null, "File Upload Completed Successfully");


    }

    public String uploadFile(MultipartFile file, Book.BookType bookType) throws IOException {
        if (file.isEmpty()) {
            // throw an exception that file is empty
        }

        // Normalize the file name to prevent directory traversal
        String fileName = StringUtils.cleanPath(
                file.getOriginalFilename() + "_" + bookType + "_" + LocalDateTime.now());


        // Create the upload directory if it doesn't exist
        Files.createDirectories(Paths.get(fileUploadLocation));

        // Save the file to the server
        Path filePath = Paths.get(fileUploadLocation).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }



    public List<Book> getClassBook(String grade, String subject, String stream){
        List<Subject> subjectList = this.subjectRepository.findByGradeAndSubjectAndStream(grade, subject, stream);
        if(subjectList.size()==0){
            return new ArrayList<>();
        }

        List<Book> bookList = this.bookRepository.findBySubjectId(subjectList.get(0).getId());
        return bookList;
    }

    public List<Book> getMCQBooks(){
        List<Book> bookList = this.bookRepository.findByBookType(Book.BookType.MCQ);
        return bookList;
    }
    public List<Book> getPublicationBooks(){
        List<Book> bookList = this.bookRepository.findByBookType(Book.BookType.PUBLICATIONS);
        return bookList;
    }

    public ResponseDto deleteBook(int id) {
        if(this.bookRepository.existsById(id)){
            this.bookRepository.deleteById(id);
        }

        return ResponseDto.Success(null, null);
    }


}