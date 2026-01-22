package com.example.spring_batch_demo.config;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DebugListener {

//    @EventListener(ApplicationReadyEvent.class)
//    public void logParams(JobExecution execution) {
//        System.out.println(execution.getJobParameters());
//    }
}