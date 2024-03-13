package com.benchmark.education.service.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Request.CreateSubjectDto;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.Subject;
import com.benchmark.education.exception.GenericWrongRequestException;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.SubjectRepository;
import com.benchmark.education.utils.BookUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminBookService {

    @Value("${protected-file-location}")
    private String protectedFileLocation;

    @Value("${public-file-location}")
    private String publicFIleLocation;

    private final SubjectRepository subjectRepository;
    private final BookRepository bookRepository;

    public AdminBookService(SubjectRepository subjectRepository, BookRepository bookRepository) {
        this.subjectRepository = subjectRepository;
        this.bookRepository = bookRepository;
    }

    public ResponseDto<String> addSubject(CreateSubjectDto dto) {
        Subject bookRegister = new Subject();
        bookRegister.setGrade(dto.getGrade());
        bookRegister.setStream(dto.getStream());
        bookRegister.setSubject(dto.getSubject());
        bookRegister.setSlug(dto.getSlug());
        bookRegister.setPrice(dto.getPrice());
        this.subjectRepository.save(bookRegister);
        return ResponseDto.Success("", null);
    }

    public ResponseDto<String> deleteSubject(int id) {
        this.subjectRepository.deleteById(id);
        return ResponseDto.Success("", null);

    }

    public ResponseDto<String> changePriceOfSubject(int id, String price){
        Optional<Subject> subjectOptional = this.subjectRepository.findById(id);
        if(subjectOptional.isEmpty()){
            // handle wrong id
            throw new GenericWrongRequestException("No subject exits");
        }

        Subject subject = subjectOptional.get();
        subject.setPrice(price);
        this.subjectRepository.save(subject);
        return ResponseDto.Success("", null);
    }



    // service logic to add book
    public ResponseDto<String> addBook(MultipartFile file,
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
        return ResponseDto.Success("", "File Upload Completed Successfully");


    }

    public String uploadFile(MultipartFile file, Book.BookType bookType) throws IOException {
        if (file.isEmpty()) {

            // throw an exception that file is empty
            throw new GenericWrongRequestException("File is Empty!");
        }
        String fileLocation = "";

        // Normalize the file name to prevent directory traversal
        String fileName = StringUtils.cleanPath(
                bookType + "_" + LocalDateTime.now()+ "_" + file.getOriginalFilename() );
        fileName = fileName.replaceAll("\\s+", "_");
        String fileUploadLocation = null;
        if(Book.BookType.PAID.equals(bookType)){
            fileUploadLocation = this.protectedFileLocation;
            fileLocation = "/files/protected/";
        } else {
            fileUploadLocation = this.publicFIleLocation;
            fileLocation = "/files/public/";
        }

        // Create the upload directory if it doesn't exist
        Files.createDirectories(Paths.get(fileUploadLocation));

        // Save the file to the server
        Path filePath = Paths.get(fileUploadLocation).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        fileLocation += fileName;
        return fileLocation;
    }

    public ResponseDto<String> deleteBook(int id) throws IOException {
        Optional<Book> bookOptional = this.bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new GenericWrongRequestException("Invalid Book Deleted");
        }
        Book book =  bookOptional.get();
        String fileName = book.getFileLocation().substring(book.getFileLocation().lastIndexOf(File.separator)+1);
        if(Book.BookType.PAID.equals(book.getBookType())){
            fileName = this.protectedFileLocation + fileName;
        } else{
            fileName = this.publicFIleLocation + fileName;
        }
        Files.delete(Path.of(new File(fileName).toURI()));
        this.bookRepository.deleteById(id);
        return ResponseDto.Success("",null);
    }


}
