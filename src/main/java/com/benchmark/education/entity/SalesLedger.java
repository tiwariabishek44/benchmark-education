package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class SalesLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String studentEmail;
    private int subjectId;

}
