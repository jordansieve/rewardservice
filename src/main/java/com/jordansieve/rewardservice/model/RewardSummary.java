package com.jordansieve.rewardservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardSummary {
    private String customerId;
    private String customerName;
    private Map<Integer, Integer> monthlyPoints;
    private int totalPoints;
}
