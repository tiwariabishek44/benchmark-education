package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String grade;
    private String subject;
    private String slug;

    private String stream;

    private String price;



}
