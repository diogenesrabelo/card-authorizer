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

class CategoryFoodTest {

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private CategoryFood categoryFood;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessTransaction_FoodBalanceSufficient() {
        Balance balance = new Balance();
        balance.setFood(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("50.00");

        TransactionResponse response = categoryFood.processTransaction(balance, amount);

        assertEquals("00", response.code(), "Expected transaction to be approved with code '00'");
        assertEquals(new BigDecimal("50.00"), balance.getFood(), "Expected balance food to be reduced by transaction amount");
        verify(balanceRepository, times(1)).saveWithLockingPessimistic(balance);
    }

    @Test
    void testProcessTransaction_FoodBalanceInsufficient_CashBalanceSufficient() {
        Balance balance = new Balance();
        balance.setFood(new BigDecimal("30.00"));
        balance.setCash(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("50.00");

        CategoryCash categoryCash = mock(CategoryCash.class);
        when(categoryCash.processTransaction(balance, amount)).thenReturn(new TransactionResponse("00"));

        TransactionResponse response = categoryFood.processTransaction(balance, amount);

        assertEquals("00", response.code(), "Expected transaction to be approved with code '00' from cash");
        verify(balanceRepository, times(1)).saveWithLockingPessimistic(balance);
    }

    @Test
    void testProcessTransaction_InsufficientBalanceBothFoodAndCash() {
        Balance balance = new Balance();
        balance.setFood(new BigDecimal("30.00"));
        balance.setCash(new BigDecimal("20.00"));
        BigDecimal amount = new BigDecimal("50.00");

        CategoryCash categoryCash = mock(CategoryCash.class);
        when(categoryCash.processTransaction(balance, amount)).thenReturn(new TransactionResponse("51"));

        TransactionResponse response = categoryFood.processTransaction(balance, amount);

        assertEquals("51", response.code(), "Expected transaction to be rejected with code '51'");
        verify(balanceRepository, never()).saveWithLockingPessimistic(balance);
    }

}