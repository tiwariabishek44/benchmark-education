package com.benchmark.education.controller;

import com.benchmark.education.entity.Book;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController("/")
public class Home {

    @Value("${file-upload-location}")
    private String fileUploadLocation;


    @GetMapping
    public String home(){
        return "from home";
    }

    @PostMapping
    public String getFile(@RequestParam("file") MultipartFile file) throws IOException {
        uploadFile(file, Book.BookType.PUBLICATIONS);
        return "success";
    }

    public String uploadFile(MultipartFile file, Book.BookType bookType) throws IOException {
        if (file.isEmpty()) {
            // throw an exception that file is empty
        }

        // Normalize the file name to prevent directory traversal
        String fileName = StringUtils.cleanPath(
                bookType + "_" + LocalDateTime.now() + file.getOriginalFilename());


        // Create the upload directory if it doesn't exist
        Files.createDirectories(Paths.get(fileUploadLocation));

        // Save the file to the server
        Path filePath = Paths.get(fileUploadLocation).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

}
