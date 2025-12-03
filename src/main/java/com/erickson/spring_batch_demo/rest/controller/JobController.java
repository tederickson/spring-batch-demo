package com.erickson.spring_batch_demo.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@Slf4j
@RequiredArgsConstructor
public class JobController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @PostMapping("/import/person")
    public String jobLauncher() {

        final JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();

        try {
            // Launch the job
            final JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            // Return job status
            return jobExecution.getStatus().toString();
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            log.error("Job failed", e);
            return "Job failed with exception: " + e.getMessage();
        }
    }
}



