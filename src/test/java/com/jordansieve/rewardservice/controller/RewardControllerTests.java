package com.jordansieve.rewardservice.controller;

import com.jordansieve.rewardservice.exception.ResourceNotFoundException;
import com.jordansieve.rewardservice.model.RewardSummary;
import com.jordansieve.rewardservice.service.RewardService;
import com.jordansieve.rewardservice.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RewardControllerTests {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @Test
    void getRewardsByCustomer_shouldReturnNotFound_whenCustomerIdIsBlank() {
        // given
        String customerId = "";

        // when
        ResponseEntity<RewardSummary> result = rewardController.getRewardsByCustomer(customerId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        verifyNoInteractions(rewardService);
    }

    @Test
    void getRewardsByCustomer_shouldReturnOk_whenValidCustomerAndDataExists() {
        // given
        String customerId = "1";
        RewardSummary rewardSummary = TestData.getRewardsSummary(customerId);
        when(rewardService.getRewardsByCustomer(customerId)).thenReturn(rewardSummary);

        // when
        ResponseEntity<RewardSummary> result = rewardController.getRewardsByCustomer(customerId);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(customerId, result.getBody().getCustomerId());
        assertEquals(rewardSummary.getTotalPoints(), result.getBody().getTotalPoints());
        verify(rewardService, times(1)).getRewardsByCustomer(customerId);
    }

    @Test
    void getRewardsByCustomer_shouldReturnNoContent_whenRewardSummaryIsNull() {
        // given
        String customerId = "1";
        when(rewardService.getRewardsByCustomer(customerId)).thenReturn(null);

        // when
        ResponseEntity<RewardSummary> result = rewardController.getRewardsByCustomer(customerId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(rewardService, times(1)).getRewardsByCustomer(customerId);
    }

    @Test
    void getRewardsByCustomer_shouldReturnNotFound_whenServiceThrowsResourceNotFoundException() {
        // given
        String customerId = "1";
        when(rewardService.getRewardsByCustomer(customerId)).thenThrow(new ResourceNotFoundException("customerId.not.found"));

        // when
        ResponseEntity<RewardSummary> result = rewardController.getRewardsByCustomer(customerId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        verify(rewardService, times(1)).getRewardsByCustomer(customerId);
    }
}
