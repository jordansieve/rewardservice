package com.jordansieve.rewardservice;

import com.jordansieve.rewardservice.model.Customer;
import com.jordansieve.rewardservice.model.Transaction;
import com.jordansieve.rewardservice.repository.CustomerRepository;
import com.jordansieve.rewardservice.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private String baseUrl;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        customerRepository.deleteAll();
        baseUrl = "http://localhost:" + port + "/api/rewards";
    }

    @Test
    void shouldReturnOk_whenCustomerExistsAndHasTransactions() {
        // given
        String customerId = "1";
        Customer customer = Customer.builder()
                .id(customerId)
                .name("Jordan")
                .build();
        customerRepository.save(customer);
        List<Transaction> transactions = TestData.getTransactions(customerId);
        transactionRepository.saveAll(transactions);

        // when
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/" + customerId, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("customerId");
        Assertions.assertThat(response.getBody()).contains("totalPoints");
    }

    @Test
    void shouldReturnNoContent_whenCustomerExistsButNoTransactions() {
        // given
        String customerId = "1";
        Customer customer = Customer.builder()
                .id(customerId)
                .name("Jordan")
                .build();
        customerRepository.save(customer);

        // when
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/" + customerId, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldReturnNotFound_whenCustomerDoesNotExist() {
        // given
        String customerId = "notReal";

        // when
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/" + customerId, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnNotFound_whenCustomerIdIsBlank() {
        // when
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/ ", String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
