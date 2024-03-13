package com.benchmark.education.service.common;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateSubjectDto;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.exception.GenericWrongRequestException;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.SalesLedgerRegister;
import com.benchmark.education.repository.SubjectRepository;
import com.benchmark.education.utils.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Service
@Transactional
public class BookService {

    @Value("${protected-file-location}")
    private String protectedFileLocation;

    @Value("${public-file-location}")
    private String publicFIleLocation;

    @Autowired
    private  SubjectRepository subjectRepository;

    @Autowired
    private  BookRepository bookRepository;

    @Autowired
    private SalesLedgerRegister salesLedgerRegister;


    public ResponseDto<List<Subject>> getallSubject(){
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



}