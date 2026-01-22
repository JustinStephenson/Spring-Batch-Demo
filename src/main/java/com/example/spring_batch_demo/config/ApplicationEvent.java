package com.example.spring_batch_demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEvent {

    private final JobOperator jobOperator;
    private final Job csvImporterJob;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("ignoreCountry", "India")
            .addString("file", "customers.csv")
            .addLong("run.id", System.currentTimeMillis())
            .toJobParameters();

        jobOperator.start(csvImporterJob, jobParameters);
    }
}
