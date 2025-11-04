package com.jordansieve.rewardservice.service;

import com.jordansieve.rewardservice.exception.ResourceNotFoundException;
import com.jordansieve.rewardservice.model.Customer;
import com.jordansieve.rewardservice.model.RewardSummary;
import com.jordansieve.rewardservice.model.Transaction;
import com.jordansieve.rewardservice.repository.CustomerRepository;
import com.jordansieve.rewardservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public RewardSummary getRewardsByCustomer(String customerId) {

        Optional<Customer> customer = customerRepository.findById(customerId);

        // throw resource not found exception, handled at controller layer
        if (customer.isEmpty()) {
            throw new ResourceNotFoundException("customerId.not.found");
        }

        List<Transaction> transactions = transactionRepository.findAllByCustomerId(customerId);

        // return null if no transactions
        if (transactions.isEmpty()) {
            return null;
        }

        return RewardSummary.builder()
                .customerId(customer.get().getId())
                .customerName(customer.get().getName())
                .monthlyPoints( getMonthlyPoints(transactions) )
                .totalPoints( getTotalPoints(transactions) )
                .build();
    }

    private Map<Integer, Integer> getMonthlyPoints(List<Transaction> transactions) {
        LocalDate now = LocalDate.now();
        LocalDate threeMonthsAgo = now.minusMonths(2).withDayOfMonth(1);

        return transactions.stream()
                // filter by last 3 months
                .filter(t -> !t.getDate().isBefore(threeMonthsAgo) && !t.getDate().isAfter(now))
                // group by month and sum the points
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getMonthValue(),
                        Collectors.summingInt(t -> calculatePointsFromAmount(t.getAmount()))
                ));
    }

    private int getTotalPoints(List<Transaction> transactions) {
        int total = 0;
        for (Transaction transaction : transactions) {
            total += calculatePointsFromAmount(transaction.getAmount());
        }
        return total;
    }

    private int calculatePointsFromAmount(double amount) {
        int points = 0;
        if (amount > 100) {
            points += 2 * (int)(amount - 100);
            points += 50; // for 50–100 range
        } else if (amount > 50) {
            points += (int)(amount - 50);
        }
        return points;
    }
}
