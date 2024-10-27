package br.com.diogenes.card_authorizer.controller.transaction.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.service.authorizer.AuthorizerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TransactionControllerImplTest {

    @Mock
    private AuthorizerService authorizerService;

    @InjectMocks
    private TransactionControllerImpl transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authorizeTransaction_shouldReturnOkStatus_whenTransactionIsAuthorized() {
        Transaction transaction = new Transaction("12345", BigDecimal.valueOf(100.00), "5812", "Supermarket");
        TransactionResponse transactionResponse = new TransactionResponse("00");

        when(authorizerService.authorizer(transaction)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = transactionController.authorizeTransaction(transaction);

        assertEquals(ResponseEntity.ok(transactionResponse), response);
        assertEquals("00", response.getBody().code());
    }

    @Test
    void authorizeTransaction_shouldReturnDeniedStatus_whenTransactionIsNotAuthorized() {
        Transaction transaction = new Transaction("12345", BigDecimal.valueOf(500.00), "5812", "Supermarket");
        TransactionResponse transactionResponse = new TransactionResponse("51");

        when(authorizerService.authorizer(transaction)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = transactionController.authorizeTransaction(transaction);

        assertEquals(ResponseEntity.ok(transactionResponse), response);
        assertEquals("51", response.getBody().code());
    }
}