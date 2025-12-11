package com.erickson.spring_batch_demo.batch.config;

import com.erickson.spring_batch_demo.batch.processor.PersonProcessor;
import com.erickson.spring_batch_demo.batch.reader.PersonLineMapper;
import com.erickson.spring_batch_demo.model.Person;
import com.erickson.spring_batch_demo.repository.PersonRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

import static com.erickson.spring_batch_demo.batch.config.JobConstant.FILE_NAME;

@Configuration
public class SpringBatchConfig {

    @Bean
    @StepScope
    FlatFileItemReader<Person> reader(
            @Value("#{jobParameters}") Map<String, Object> jobParameters,
            PersonLineMapper personLineMapper) {
        String fileName = jobParameters.get(FILE_NAME).toString();

        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource(fileName))
                .linesToSkip(1)
                .lineMapper(personLineMapper)
                .targetType(Person.class)
                .build();
    }

    @Bean
    RepositoryItemWriter<Person> writer(PersonRepository personRepository) {
        RepositoryItemWriter<Person> writer = new RepositoryItemWriter<>();
        writer.setRepository(personRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("importPersons", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    Step step(JobRepository jobRepository,
              PlatformTransactionManager transactionManager,
              PersonProcessor processor,
              FlatFileItemReader<Person> reader,
              RepositoryItemWriter<Person> writer) {
        return new StepBuilder("csv-import-step", jobRepository)
                .<Person, Person>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
