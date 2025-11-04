package com.jordansieve.rewardservice;

import com.jordansieve.rewardservice.model.RewardSummary;
import com.jordansieve.rewardservice.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TestData {

    public static List<Transaction> getTransactions(String customerId) {
        Random random = new Random();
        int totalTransactions = random.nextInt(20) + 5;
        List<Transaction> transactions = new ArrayList<>();

        // used to generate a random day within the last 90 days
        long oneYearAgo = LocalDate.now().toEpochDay() - 90;
        long today = LocalDate.now().toEpochDay();

        for (int i = 0; i < totalTransactions; i++) {
            // generate a random transaction amount with 2 decimal places
            double randomAmount = random.nextDouble() * 1000;
            randomAmount = (double) Math.round(randomAmount * 100) / 100;

            // generate a random day within the last year
            long randomDay = random.nextLong(oneYearAgo, today);

            transactions.add(Transaction.builder()
                    .id(UUID.randomUUID().toString())
                    .customerId(customerId)
                    .date(LocalDate.ofEpochDay(randomDay))
                    .amount(randomAmount)
                    .build());
        }

        return transactions;
    }

    private static Map<Integer, Integer> getMonthlyPoints(List<Transaction> transactions) {

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        // create Map with the last 3 months as options
        Map<Integer, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put(currentMonth, 0);
        monthlyPoints.put(currentMonth - 1, 0);
        monthlyPoints.put(currentMonth - 2, 0);

        for (Transaction transaction : transactions) {

            int transactionMonth = transaction.getDate().getMonthValue();

            if (monthlyPoints.containsKey(transactionMonth)) {
                int currentPoints = monthlyPoints.get(transactionMonth);
                currentPoints += getTransactionPoints(transaction.getAmount());
                monthlyPoints.put(transactionMonth, currentPoints);
            }
        }

        return monthlyPoints;
    }

    public static int getPointsTotal(List<Transaction> transactions) {
        return transactions.stream()
                .mapToInt(t -> getTransactionPoints(t.getAmount()))
                .sum();
    }

    private static int getTransactionPoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += 2 * (int)(amount - 100);
            points += 50; // for 50–100 range
        } else if (amount > 50) {
            points += (int)(amount - 50);
        }
        return points;
    }

    public static RewardSummary getRewardsSummary(String customerId) {
        String customerName = "Jordan";
        List<Transaction> transactions = getTransactions(customerId);

        return RewardSummary.builder()
                .customerId(customerId)
                .customerName(customerName)
                .monthlyPoints(getMonthlyPoints(transactions))
                .totalPoints(getPointsTotal(transactions))
                .build();
    }
}
