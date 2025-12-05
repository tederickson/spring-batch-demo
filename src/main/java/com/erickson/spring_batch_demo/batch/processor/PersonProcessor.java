package com.erickson.spring_batch_demo.batch.processor;

import com.erickson.spring_batch_demo.model.Person;
import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import static com.erickson.spring_batch_demo.batch.config.JobConstant.INSERT_COUNT;
import static com.erickson.spring_batch_demo.batch.config.JobConstant.INVALID_ID;
import static com.erickson.spring_batch_demo.batch.config.JobConstant.REJECT_COUNT;

@Component
public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) {
        if (person.getUserId().equals(INVALID_ID)) {
            incrementCount(REJECT_COUNT);
            return null;
        }

        incrementCount(INSERT_COUNT);
        return person;
    }

    private void incrementCount(String key) {
        ExecutionContext executionContext =
                JobSynchronizationManager.getContext().getJobExecution().getExecutionContext();

        Integer count = executionContext.get(key, Integer.class, 0);

        executionContext.put(key, count + 1);
    }
}
