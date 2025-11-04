package com.jordansieve.rewardservice.service;

import com.jordansieve.rewardservice.exception.ResourceNotFoundException;
import com.jordansieve.rewardservice.model.Customer;
import com.jordansieve.rewardservice.model.RewardSummary;
import com.jordansieve.rewardservice.model.Transaction;
import com.jordansieve.rewardservice.repository.CustomerRepository;
import com.jordansieve.rewardservice.repository.TransactionRepository;
import com.jordansieve.rewardservice.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RewardServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void getRewardsByCustomer_shouldThrowResourceNotFoundException_whenCustomerNotFound() {
        // given
        String customerId = "1";
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // when
        ResourceNotFoundException result = assertThrows(
                ResourceNotFoundException.class,
                () -> rewardService.getRewardsByCustomer(customerId)
        );

        // then
        assertEquals("customerId.not.found", result.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void getRewardsByCustomer_shouldReturnNull_whenNoTransactions() {
        // given
        String customerId = "1";
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(transactionRepository.findAllByCustomerId(customerId)).thenReturn(List.of());

        // when
        RewardSummary result = rewardService.getRewardsByCustomer(customerId);

        // then
        assertNull(result);
        verify(customerRepository).findById(customerId);
        verify(transactionRepository).findAllByCustomerId(customerId);
    }

    @Test
    void getRewardsByCustomer_shouldReturnValidSummary_whenTransactionsExist() {
        // given
        String customerId = "1";
        Customer customer = Customer.builder()
                .id(customerId)
                .name("Jordan")
                .build();
        List<Transaction> transactions = TestData.getTransactions(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findAllByCustomerId(customerId)).thenReturn(transactions);

        // when
        RewardSummary result = rewardService.getRewardsByCustomer(customerId);

        // then
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals("Jordan", result.getCustomerName());
        assertEquals(TestData.getPointsTotal(transactions), result.getTotalPoints());
        Map<Integer, Integer> monthlyPoints = result.getMonthlyPoints();
        assertTrue(monthlyPoints.size() <= 3);
        assertTrue(monthlyPoints.values().stream().mapToInt(Integer::intValue).sum() > 0);
    }
}
