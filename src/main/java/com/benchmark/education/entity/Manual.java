package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Manual")
public class Manual {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private int subjectId;

    private String name;
    private String description;
    private String fileLocation;
}
