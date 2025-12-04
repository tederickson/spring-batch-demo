package com.erickson.spring_batch_demo.batch.config;

import com.erickson.spring_batch_demo.model.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import static com.erickson.spring_batch_demo.batch.config.JobConstant.INVALID_ID;

@Component
public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) {
        if (person.getUserId().equals(INVALID_ID)) {return null;}

        return person;
    }
}
