package com.erickson.spring_batch_demo.batch.reader;

import com.erickson.spring_batch_demo.batch.mapper.LocalDatePropertyEditor;
import com.erickson.spring_batch_demo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditor;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

        Map<Class<?>, PropertyEditor> customEditors = new HashMap<>();

        customEditors.put(LocalDate.class, new LocalDatePropertyEditor("M/d/yy"));
        fieldSetMapper.setCustomEditors(customEditors);
    }

    @Override
    public @NonNull Person mapLine(@NonNull String line, int lineNumber) {
        FieldSet fieldSet = lineTokenizer.tokenize(line);

        try {
            if (validString("userId", fieldSet, line, lineNumber) &&
                    validString("firstName", fieldSet, line, lineNumber) &&
                    validString("lastName", fieldSet, line, lineNumber) &&
                    validString("jobTitle", fieldSet, line, lineNumber) &&
                    validGender(fieldSet.readString("gender"), line, lineNumber) &&
                    validEmail(fieldSet.readString("email"), line, lineNumber) &&
                    validPhone(fieldSet.readString("phone"), line, lineNumber)) {
                return fieldSetMapper.mapFieldSet(fieldSet);
            }
        } catch (Exception e) {
            invalidLine(line, lineNumber, "invalid dateOfBirth");
        }

        Person invalidPerson = new Person();
        invalidPerson.setUserId(INVALID_ID);

        return invalidPerson;
    }

    private boolean validEmail(String email, String line, int lineNumber) {
        if (email.contains("@")) {
            return true;
        }

        invalidLine(line, lineNumber, "invalid email [" + email + "]");
        return false;
    }

    private boolean validGender(String gender, String line, int lineNumber) {
        // This needs to expand to include LGBTQ+ genders
        if ("Male".equals(gender) || "Female".equals(gender)) {
            return true;
        }

        invalidLine(line, lineNumber, "invalid gender [" + gender + "]");

        return false;
    }

    private boolean validString(String key, FieldSet fieldSet, String line, int lineNumber) {
        String value = fieldSet.readString(key);

        if (value.isBlank()) {
            invalidLine(line, lineNumber, "missing " + key);
            return false;
        }
        return true;
    }

    private boolean validPhone(String phone, String line, int lineNumber) {
        if (phone.isBlank()) {
            invalidLine(line, lineNumber, "missing phone");
            return false;
        }
        if (phone.startsWith("-")) {
            invalidLine(line, lineNumber, "invalid phone");
            return false;
        }
        return true;
    }

    private void invalidLine(String line, int lineNumber, String errorMessage) {
        log.error("{} on line {} : {}", errorMessage, lineNumber, line);
    }
}
