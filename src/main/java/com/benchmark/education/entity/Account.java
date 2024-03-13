package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Entity()
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Stream stream;

    @Column(nullable = true)
    private LocalDate createdDate;

    @Column(nullable = true)
    private Boolean isActive;

    @Column(nullable = true)
    private Boolean isVerified;

    @PrePersist()
    public void setCreationDate(){
      this.createdDate = LocalDate.now();
      this.isActive = true;
    }

    public enum AccountType{
      STUDENT, TEACHER, ADMIN;
  }

  public enum Stream{
    SCIENCE, MANAGEMENT;
  }

}
