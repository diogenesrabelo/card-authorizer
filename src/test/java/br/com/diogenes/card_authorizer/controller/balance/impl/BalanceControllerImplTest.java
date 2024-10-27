package br.com.diogenes.card_authorizer.controller.balance.impl;

import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.balance.BalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceControllerImplTest {

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceControllerImpl balanceController;

    @Test
    void testCreateBalance() {
        Balance balance = new Balance().buildBalance("123", BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(75));
        when(balanceService.saveOrUpdateBalance(balance)).thenReturn(balance);

        ResponseEntity<Balance> response = balanceController.createBalance(balance);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(balance, response.getBody());
        verify(balanceService, times(1)).saveOrUpdateBalance(balance);
    }

    @Test
    void testDepositToAccount_Success() {
        String account = "123";
        BigDecimal meal = BigDecimal.valueOf(10);
        BigDecimal cash = BigDecimal.valueOf(20);
        BigDecimal food = BigDecimal.valueOf(15);

        Balance updatedBalance = new Balance().buildBalance(account, BigDecimal.valueOf(110), BigDecimal.valueOf(70), BigDecimal.valueOf(90));
        when(balanceService.depositToAccount(account, meal, cash, food)).thenReturn(Optional.of(updatedBalance));

        ResponseEntity<Balance> response = balanceController.depositToAccount(account, meal, cash, food);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBalance, response.getBody());
        verify(balanceService, times(1)).depositToAccount(account, meal, cash, food);
    }

    @Test
    void testDepositToAccount_AccountNotFound() {
        String account = "123";
        BigDecimal meal = BigDecimal.valueOf(10);
        BigDecimal cash = BigDecimal.valueOf(20);
        BigDecimal food = BigDecimal.valueOf(15);

        when(balanceService.depositToAccount(account, meal, cash, food)).thenReturn(Optional.empty());

        ResponseEntity<Balance> response = balanceController.depositToAccount(account, meal, cash, food);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(balanceService, times(1)).depositToAccount(account, meal, cash, food);
    }

    @Test
    void testGetAllBalances() {
        Pageable pageable = PageRequest.of(0, 10);
        Balance balance1 = new Balance().buildBalance("123", BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(75));
        Balance balance2 = new Balance().buildBalance("124", BigDecimal.valueOf(200), BigDecimal.valueOf(150), BigDecimal.valueOf(125));
        Page<Balance> balancePage = new PageImpl<>(List.of(balance1, balance2));

        when(balanceService.getAllBalances(pageable)).thenReturn(balancePage);

        Page<Balance> response = balanceController.getAllBalances(pageable);

        assertEquals(2, response.getTotalElements());
        assertEquals(balancePage, response);
        verify(balanceService, times(1)).getAllBalances(pageable);
    }

    @Test
    void testGetBalanceByAccount_Success() {
        String account = "123";
        Balance balance = new Balance().buildBalance(account, BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(75));
        Optional<Balance> optBalance = Optional.of(balance);

        when(balanceService.findBalance(account)).thenReturn(optBalance);

        ResponseEntity<Balance> response = balanceController.getBalanceByAccount(account);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balance, response.getBody());
        verify(balanceService, times(1)).findBalance(account);
    }

    @Test
    void testGetBalanceByAccount_NotFound() {
        String account = "99999";

        when(balanceService.findBalance(account)).thenReturn(Optional.empty());

        var response = balanceController.getBalanceByAccount(account);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(balanceService, times(1)).findBalance(account);
    }
}