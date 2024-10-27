package br.com.diogenes.card_authorizer.service.authorizer.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.balance.BalanceService;
import br.com.diogenes.card_authorizer.service.category.Category;
import br.com.diogenes.card_authorizer.service.category.factory.CategoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorizerServiceImplTest {

    @Mock
    private BalanceService balanceService;

    @Mock
    private CategoryFactory categoryFactory;

    @Mock
    private Category category;

    @InjectMocks
    private AuthorizerServiceImpl authorizerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizer_SuccessfulTransaction() {
        Transaction transaction = new Transaction("123", BigDecimal.valueOf(100), "5411", "Merchant1");
        Balance balance = new Balance();
        Optional<Balance> optBalance = Optional.of(balance);
        TransactionResponse expectedResponse = new TransactionResponse("00");

        when(balanceService.findBalance(transaction.account())).thenReturn(optBalance);
        when(categoryFactory.getCategoryFromMCC(transaction.mcc(), transaction.merchant())).thenReturn(category);
        when(category.processTransaction(balance, transaction.totalAmount())).thenReturn(expectedResponse);

        TransactionResponse response = authorizerService.authorizer(transaction);

        assertEquals(expectedResponse, response, "Expected successful transaction response code '00'");
        verify(balanceService, times(1)).findBalance(transaction.account());
        verify(categoryFactory, times(1)).getCategoryFromMCC(transaction.mcc(), transaction.merchant());
        verify(category, times(1)).processTransaction(balance, transaction.totalAmount());
    }

    @Test
    void testAuthorizer_BalanceNotFound() {
        Transaction transaction = new Transaction("123", BigDecimal.valueOf(100), "5411", "Merchant1");

        when(balanceService.findBalance(transaction.account())).thenThrow(new RuntimeException("Balance not found"));

        TransactionResponse response = authorizerService.authorizer(transaction);

        assertEquals("07", response.code(), "Expected error response code '07' when balance not found");
        verify(balanceService, times(1)).findBalance(transaction.account());
        verify(categoryFactory, never()).getCategoryFromMCC(anyString(), anyString());
        verify(category, never()).processTransaction(any(Balance.class), any(BigDecimal.class));
    }

    @Test
    void testAuthorizer_CategoryNotFound() {
        Transaction transaction = new Transaction("123", BigDecimal.valueOf(100), "5411", "Merchant1");
        Balance balance = new Balance();
        Optional<Balance> optBalance = Optional.of(balance);

        when(balanceService.findBalance(transaction.account())).thenReturn(optBalance);
        when(categoryFactory.getCategoryFromMCC(transaction.mcc(), transaction.merchant())).thenThrow(new RuntimeException("Category not found"));

        TransactionResponse response = authorizerService.authorizer(transaction);

        assertEquals("07", response.code(), "Expected error response code '07' when category not found");
        verify(balanceService, times(1)).findBalance(transaction.account());
        verify(categoryFactory, times(1)).getCategoryFromMCC(transaction.mcc(), transaction.merchant());
        verify(category, never()).processTransaction(any(Balance.class), any(BigDecimal.class));
    }

    @Test
    void testAuthorizer_ProcessTransactionFails() {
        Transaction transaction = new Transaction("123", BigDecimal.valueOf(100), "5411", "Merchant1");
        Balance balance = new Balance();
        Optional<Balance> optBalance = Optional.of(balance);

        when(balanceService.findBalance(transaction.account())).thenReturn(optBalance);
        when(categoryFactory.getCategoryFromMCC(transaction.mcc(), transaction.merchant())).thenReturn(category);
        when(category.processTransaction(balance, transaction.totalAmount())).thenThrow(new RuntimeException("Transaction processing failed"));

        TransactionResponse response = authorizerService.authorizer(transaction);

        assertEquals("07", response.code(), "Expected error response code '07' when transaction processing fails");
        verify(balanceService, times(1)).findBalance(transaction.account());
        verify(categoryFactory, times(1)).getCategoryFromMCC(transaction.mcc(), transaction.merchant());
        verify(category, times(1)).processTransaction(balance, transaction.totalAmount());
    }
}