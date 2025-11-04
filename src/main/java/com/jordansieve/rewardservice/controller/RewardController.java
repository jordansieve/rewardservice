package com.jordansieve.rewardservice.controller;

import com.jordansieve.rewardservice.exception.ResourceNotFoundException;
import com.jordansieve.rewardservice.model.RewardSummary;
import com.jordansieve.rewardservice.service.RewardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardSummary> getRewardsByCustomer(@PathVariable String customerId) {
        if (customerId.isBlank()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            RewardSummary rewardSummary = rewardService.getRewardsByCustomer(customerId);

            // return a 204 if existing customer has no transactions
            if (rewardSummary == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(rewardSummary, HttpStatus.OK);
        } catch(ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
