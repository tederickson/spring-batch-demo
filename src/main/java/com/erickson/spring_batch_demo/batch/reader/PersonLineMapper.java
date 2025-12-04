package com.erickson.spring_batch_demo.batch.reader;

import com.erickson.spring_batch_demo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Component;

import static com.erickson.spring_batch_demo.batch.config.JobConstant.INVALID_ID;

@Component
@Slf4j
public class PersonLineMapper implements LineMapper<Person> {
    private final DelimitedLineTokenizer lineTokenizer;
    private final BeanWrapperFieldSetMapper<Person> fieldSetMapper;

    public PersonLineMapper() {
        lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("userId", "firstName", "lastName", "gender", "email", "phone", "dateOfBirth",
                               "jobTitle");

        fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);
    }

    @Override
    public Person mapLine(String line, int lineNumber) throws Exception {
        Person person = fieldSetMapper.mapFieldSet(lineTokenizer.tokenize(line));

        if (person.getPhone() == null) {
            log.error("missing phone");
            invalidLine(line, lineNumber, "missing phone", person);
        }
        if (person.getPhone().startsWith("-")) {
            invalidLine(line, lineNumber, "invalid phone", person);
        }
        return person;
    }

    private void invalidLine(String line, int lineNumber, String errorMessage, Person person) {
        log.error("{} on line {} : {}", errorMessage, lineNumber, line);
        person.setUserId(INVALID_ID);
    }
}
