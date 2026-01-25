package com.erickson.spring_batch_demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Person {
    @Id
    private String userId;

    @Version // Enable optimistic locking
    private int version;

    @Column(nullable = false, length = 40)
    private String firstName;

    @Column(nullable = false, length = 40)
    private String lastName;

    @Column(nullable = false, length = 6)
    private String gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String jobTitle;
}
