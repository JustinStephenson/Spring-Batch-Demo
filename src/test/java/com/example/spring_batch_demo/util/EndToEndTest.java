package com.example.spring_batch_demo.util;

import org.flywaydb.core.Flyway;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest(properties = "spring.profiles.active=test")
@SpringBatchTest
@Testcontainers
public class EndToEndTest {

    private static final String POSTGRES_CONTAINER = "postgres:latest";
    private static final String POSTGRES_DATABASE = "test-database";
    private static final String POSTGRES_USERNAME = "postgres";
    private static final String POSTGRES_PASSWORD = "postgres";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_CONTAINER)
        .withDatabaseName(POSTGRES_DATABASE)
        .withUsername(POSTGRES_USERNAME)
        .withPassword(POSTGRES_PASSWORD);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Postgres configs
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Flyway migrations location
        registry.add("spring.flyway.locations", () -> "classpath:db/migrations");
    }

    static {
        Startables.deepStart(postgres).join();

        Flyway flyway = Flyway.configure()
            .dataSource(postgres.getJdbcUrl(), POSTGRES_USERNAME, POSTGRES_PASSWORD)
            .locations("classpath:db/migrations")
            .load();
        flyway.migrate();
    }
}
