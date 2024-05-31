package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="SalesLedger")
public class SalesLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private int subjectId;

}
