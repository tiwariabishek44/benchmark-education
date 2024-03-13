package com.benchmark.education.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EcomerceEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn()
    private Account account;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn()
    private Book book;
    private String message;
}
