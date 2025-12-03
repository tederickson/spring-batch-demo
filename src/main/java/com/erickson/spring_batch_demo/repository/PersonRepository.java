package com.erickson.spring_batch_demo.repository;

import com.erickson.spring_batch_demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
