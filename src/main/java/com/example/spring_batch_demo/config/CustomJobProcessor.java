package com.example.spring_batch_demo.config;

import com.example.spring_batch_demo.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@JobScope
@Component
public class CustomJobProcessor implements ItemProcessor<Customer, Customer> {

    private final String ignoreCountry;

    public CustomJobProcessor(@Value("#{jobParameters['ignoreCountry']}") String ignoreCountry) {
        this.ignoreCountry = ignoreCountry;
    }

    @Override
    // Will be called after item reader
    public Customer process(Customer item) {
        if (ignoreCountry.equalsIgnoreCase(item.getCountry())) {
            log.info("Customer ignored: {}", item);
            return null;
        }
        return item;
    }
}
