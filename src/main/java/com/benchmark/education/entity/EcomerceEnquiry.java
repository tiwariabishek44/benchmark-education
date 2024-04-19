package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EcomerceEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;


    private String name;
    private String phoneNumber;

   private int bookId;
    private String message;

    private LocalDateTime datetime;

    @PrePersist
    private void setDateTime(){
        this.datetime = LocalDateTime.now();
        System.out.println(EcomerceEnquiry.class  +" : " + this.bookId);
    }

}
