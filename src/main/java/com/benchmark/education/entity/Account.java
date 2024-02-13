package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity()
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String phoneNumber;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    private Stream stream;

    private LocalDate createdDate;
    private boolean isActive;

    private boolean isVerified;

    @PrePersist()
    public void setCreationDate(){
      this.createdDate = LocalDate.now();
      this.isActive = true;
    }

    public enum AccountType{
      STUDENT, TEACHER;
  }

  public enum Stream{
    STUDENT, TEACHER;
  }

}
