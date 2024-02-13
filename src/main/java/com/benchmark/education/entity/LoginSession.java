package com.benchmark.education.entity;

import jakarta.persistence.Id;
import lombok.Data;


@Data
public class LoginSession {

    @Id
    private int userId;

    private String sessionHash;
}
