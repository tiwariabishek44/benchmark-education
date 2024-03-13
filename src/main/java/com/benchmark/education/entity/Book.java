package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private int subjectId;
    private String name;
    private String description;
    private String fileLocation;
    private String price;

    @Enumerated(EnumType.STRING)
    private BookType bookType;

    public enum BookType{
        MCQ, FREE, PAID, PUBLICATIONS;
    }
}
