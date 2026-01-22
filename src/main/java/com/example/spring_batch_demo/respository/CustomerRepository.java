package com.example.spring_batch_demo.respository;

import com.example.spring_batch_demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
