package br.com.diogenes.card_authorizer.controller.transaction.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.authorizer.AuthorizerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@Transactional
class TransactionControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BalanceRepository balanceRepository;

    @Mock
    private AuthorizerService authorizerService;

    @InjectMocks
    private TransactionControllerImpl transactionController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        balanceRepository.deleteAll();
        Balance balance = new Balance().buildBalance("12345", BigDecimal.valueOf(100), BigDecimal.valueOf(100), BigDecimal.valueOf(100));
        balanceRepository.save(balance);
    }

    @Test
    void authorizeTransaction_shouldReturnOk_whenTransactionIsAuthorized() throws Exception {
        Transaction transaction = new Transaction("12345", BigDecimal.valueOf(100.00), "5812", "Supermarket");
        TransactionResponse transactionResponse = new TransactionResponse("00");

        when(authorizerService.authorizer(any(Transaction.class))).thenReturn(transactionResponse);

        String transactionRequestJson = objectMapper.writeValueAsString(transaction);

        mockMvc.perform(post("/api/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionRequestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void authorizeTransaction_shouldReturnDeniedStatus_whenTransactionIsNotAuthorized() throws Exception {
        Transaction transaction = new Transaction("12345", BigDecimal.valueOf(500.00), "5812", "Supermarket");
        TransactionResponse transactionResponse = new TransactionResponse("51");

        when(authorizerService.authorizer(any(Transaction.class))).thenReturn(transactionResponse);

        String transactionResponseJson = objectMapper.writeValueAsString(transactionResponse);
        String transactionRequestJson = objectMapper.writeValueAsString(transaction);

        mockMvc.perform(post("/api/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionRequestJson))
            .andExpect(status().isOk())
            .andExpect(content().json(transactionResponseJson));
    }
}