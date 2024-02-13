package com.benchmark.education.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {

    @Id
    private int id;

    private int bookRegisterId;
    private String name;
    private String description;
    private String fileLocation;

    @Enumerated(EnumType.STRING)
    private BookType bookType;

    public enum BookType{
        MCQ, FREE, PAID;
    }
}
