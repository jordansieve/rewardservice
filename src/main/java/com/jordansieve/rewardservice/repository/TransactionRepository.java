package com.jordansieve.rewardservice.repository;

import com.jordansieve.rewardservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findAllByCustomerId(String customerId);
}
