package com.benchmark.education.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class BookRegister {

    private int id;

    private String grade;
    private String subject;
    private String slug;

    private String price;



}
