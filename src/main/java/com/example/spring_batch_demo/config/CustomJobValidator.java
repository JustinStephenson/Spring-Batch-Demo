package com.example.spring_batch_demo.config;

import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class CustomJobValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws InvalidJobParametersException {
        if ("Italy".equalsIgnoreCase(parameters.getString("ignoreCountry"))) {
            throw new InvalidJobParametersException("Invalid configuration");
        }
    }
}
