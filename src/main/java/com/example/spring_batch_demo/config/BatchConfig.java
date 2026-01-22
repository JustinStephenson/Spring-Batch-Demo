package com.example.spring_batch_demo.config;


import com.example.spring_batch_demo.model.Customer;
import com.example.spring_batch_demo.utils.DateUtils;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Customer> reader() {
        return new FlatFileItemReaderBuilder<Customer>()
            .linesToSkip(1)
            .name("csvItemReader")
            .resource(new ClassPathResource("customers.csv"))
            .delimited()
            .delimiter(",")
            .names(new String[]{"index", "customerId", "firstName", "lastName", "company", "city", "country", "phone1", "phone2", "email", "subscriptionDate", "website"})
            .fieldSetMapper(fieldSet -> Customer.builder()
                .customerId(fieldSet.readString("customerId"))
                .firstName(fieldSet.readString("firstName"))
                .lastName(fieldSet.readString("lastName"))
                .company(fieldSet.readString("company"))
                .city(fieldSet.readString("city"))
                .country(fieldSet.readString("country"))
                .phone1(fieldSet.readString("phone1"))
                .phone2(fieldSet.readString("phone2"))
                .email(fieldSet.readString("email"))
                .subscriptionDate(DateUtils.parseDate(fieldSet.readString("subscriptionDate")))
                .website(fieldSet.readString("website"))
                .build())
            .build();
    }

    @Bean
    public JpaItemWriter<Customer> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Customer>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }


    @Bean
    public Step csvImporterStep(ItemReader<Customer> reader, ItemWriter<Customer> writer, JobRepository jobRepository,
                                PlatformTransactionManager platformTransactionManager, CustomJobProcessor customJobProcessor) {
        return new StepBuilder("csvImporterStep", jobRepository)
            .<Customer, Customer>chunk(50)
            .reader(reader)
            .processor(customJobProcessor)
            .writer(writer)
            .transactionManager(platformTransactionManager)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Job csvImporterJob(Step csvImporterStep, JobRepository jobRepository, ImportJobListener importJobListener, CustomJobValidator customJobValidator) {
        return new JobBuilder("csvImporterJob", jobRepository)
            .listener(importJobListener)
            .validator(customJobValidator)
            .flow(csvImporterStep) // Can add more steps with .next()
            .end()
            .build();
    }
}
