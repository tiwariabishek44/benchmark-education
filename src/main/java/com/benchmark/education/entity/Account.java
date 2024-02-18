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
public class Account {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;

    @Nullable
    private String phoneNumber;
    private String name;

    @Column(nullable = false)
    private String password;

    @Nullable
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private Stream stream;

    private LocalDate createdDate;

    @Nullable
    private boolean isActive;

    @Nullable
    private boolean isVerified;

    @PrePersist()
    public void setCreationDate(){
      this.createdDate = LocalDate.now();
      this.isActive = true;
    }

    public enum AccountType{
      STUDENT, TEACHER, ADMIN;
  }

  public enum Stream{
    STUDENT, TEACHER;
  }

}
