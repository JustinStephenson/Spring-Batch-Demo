package com.example.spring_batch_demo.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    public DatabaseConfig(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    @Primary
    @Override
    @NonNull
    @SuppressWarnings({"removal"})
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(this.dataSource);
        factory.setTransactionManager(getTransactionManager());
        try {
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Batch JobRepository", e);
        }
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(this.entityManagerFactory);
    }

    @Override
    @NonNull
    protected PlatformTransactionManager getTransactionManager() {
        return transactionManager();
    }
}
