package com.example.spring_batch_demo.e2e;

import com.example.spring_batch_demo.model.Customer;
import com.example.spring_batch_demo.util.EndToEndTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvImporterJobEndToEndTest extends EndToEndTest {

    @Autowired(required = false)
    @SuppressWarnings({"removal"})
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private EntityManager entityManager;

    @Test
    @SuppressWarnings({"removal"})
    void shouldImportCustomersFromCsv() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("ignoreCountry", "India")
            .addString("file", "files/customers.csv")
            .addLong("run.id", System.currentTimeMillis())
            .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getStatus())
            .as("Job should complete successfully")
            .isEqualTo(BatchStatus.COMPLETED);

        List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
            .getResultList();

        assertThat(customers)
            .extracting(Customer::getCountry)
            .doesNotContain("India");
        assertThat(customers)
            .as("Customers should be imported from CSV")
            .isNotEmpty();
        assertThat(customers)
            .extracting(Customer::getCustomerId)
            .doesNotContain("8C62C0FEdE14178")
            .contains("4962fdbE6Bfee6D", "9b12Ae76fdBc9bE");
    }
}
