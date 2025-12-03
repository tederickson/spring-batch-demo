package com.erickson.spring_batch_demo.config;

import com.erickson.spring_batch_demo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) {

        person.setId(null);
        person.setFirstName(person.getFirstName().toUpperCase());
        person.setLastName(person.getLastName().toUpperCase());

        log.info("{}", person);

        return person;
    }
}
