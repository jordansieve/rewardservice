package com.jordansieve.rewardservice.repository;

import com.jordansieve.rewardservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {}