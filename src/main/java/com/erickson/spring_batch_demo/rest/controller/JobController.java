package com.erickson.spring_batch_demo.rest.controller;

import com.erickson.spring_batch_demo.exception.ImportJobException;
import com.erickson.spring_batch_demo.rest.domain.ImportRequest;
import com.erickson.spring_batch_demo.rest.domain.ImportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.erickson.spring_batch_demo.batch.config.JobConstant.FILE_NAME;
import static com.erickson.spring_batch_demo.batch.config.JobConstant.INSERT_COUNT;
import static com.erickson.spring_batch_demo.batch.config.JobConstant.REJECT_COUNT;

@RestController
@RequestMapping("/jobs")
@Slf4j
@RequiredArgsConstructor
public class JobController {
    private final JobLauncher jobLauncher;
    private final Job job;

    private static void validateRequest(ImportRequest importRequest) throws ImportJobException {
        if (!StringUtils.hasText(importRequest.fileName())) {
            throw new ImportJobException("Missing file name", HttpStatus.BAD_REQUEST);
        }

        var resource = new ClassPathResource(importRequest.fileName());
        if (!resource.exists()) {
            throw new ImportJobException("Invalid file name " + importRequest.fileName(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/import/person")
    public ImportResponse importPerson(@RequestBody ImportRequest importRequest) throws ImportJobException {
        final long startAt = System.currentTimeMillis();

        validateRequest(importRequest);

        final JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", startAt)
                .addString(FILE_NAME, importRequest.fileName())
                .toJobParameters();

        try {
            // Launch the job
            final JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            // Return job status
            BatchStatus status = jobExecution.getStatus();
            Integer insertCount = jobExecution.getExecutionContext().getInt(INSERT_COUNT, 0);
            Integer rejectCount = jobExecution.getExecutionContext().getInt(REJECT_COUNT, 0);
            Long duration = System.currentTimeMillis() - startAt;

            return new ImportResponse(status, insertCount, rejectCount, duration);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            log.error("job failed", e);
            throw new ImportJobException("Job failed with exception: " + e.getMessage(),
                                         HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



