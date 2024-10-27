package br.com.diogenes.card_authorizer.service.category.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryCashTest {

    @InjectMocks
    private CategoryCash categoryCash;

    @Mock
    private BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessTransaction_Approved() {
        Balance balance = new Balance();
        balance.setCash(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("50.00");

        TransactionResponse response = categoryCash.processTransaction(balance, amount);

        assertEquals("00", response.code(), "Expected transaction to be approved with code '00'");
        assertEquals(new BigDecimal("50.00"), balance.getCash(), "Expected balance cash to be reduced by transaction amount");
        verify(balanceRepository, times(1)).saveWithLockingPessimistic(balance);
    }

    @Test
    void testProcessTransaction_InsufficientBalance() {
        Balance balance = new Balance();
        balance.setCash(new BigDecimal("30.00"));
        BigDecimal amount = new BigDecimal("50.00");

        TransactionResponse response = categoryCash.processTransaction(balance, amount);

        assertEquals("51", response.code(), "Expected transaction to be rejected with code '51'");
        assertEquals(new BigDecimal("30.00"), balance.getCash(), "Expected balance cash to remain unchanged");
        verify(balanceRepository, never()).saveWithLockingPessimistic(balance);
    }
}