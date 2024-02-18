package com.benchmark.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LoginSession {

    @Id
    private int userId;

    private String sessionHash;
}
