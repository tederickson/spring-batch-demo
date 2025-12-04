package com.erickson.spring_batch_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Person {
    @Id
    private String userId;

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String jobTitle;
}
