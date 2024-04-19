package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ForgetPasswordTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String email;
    private int otpCode;
}
