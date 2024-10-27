package br.com.diogenes.card_authorizer.service.balance.impl;

import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BalanceServiceImplTest {

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrUpdateBalance() {
        Balance balance = new Balance();
        when(balanceRepository.saveWithLockingPessimistic(balance)).thenReturn(balance);

        Balance result = balanceService.saveOrUpdateBalance(balance);

        assertEquals(balance, result, "Expected the saved balance to be returned");
        verify(balanceRepository, times(1)).saveWithLockingPessimistic(balance);
    }

    @Test
    void testFindBalance_BalanceExists() {
        Balance balance = new Balance();
        String accountId = "123";
        when(balanceRepository.findById(accountId)).thenReturn(Optional.of(balance));

        Balance result = balanceService.findBalance(accountId).get();

        assertEquals(balance, result, "Expected to find the balance with the provided account ID");
        verify(balanceRepository, times(1)).findById(accountId);
    }

    @Test
    void testFindBalance_BalanceNotFound() {
        String accountId = "123";
        when(balanceRepository.findById(accountId)).thenReturn(Optional.empty());

        var balance = balanceService.findBalance(accountId);

        assertTrue(balance.isEmpty());
        verify(balanceRepository, times(1)).findById(accountId);
    }

    @Test
    void testDepositToAccount_BalanceExists() {
        String accountId = "123";
        Balance balance = new Balance();
        balance.setMeal(BigDecimal.ZERO);
        balance.setCash(BigDecimal.ZERO);
        balance.setFood(BigDecimal.ZERO);

        when(balanceRepository.findById(accountId)).thenReturn(Optional.of(balance));

        Optional<Balance> result = balanceService.depositToAccount(accountId, BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(75));

        assertTrue(result.isPresent(), "Expected to find the balance for deposit");
        assertEquals(BigDecimal.valueOf(100), balance.getMeal(), "Expected meal balance to be updated");
        assertEquals(BigDecimal.valueOf(50), balance.getCash(), "Expected cash balance to be updated");
        assertEquals(BigDecimal.valueOf(75), balance.getFood(), "Expected food balance to be updated");
        verify(balanceRepository, times(1)).findById(accountId);
        verify(balanceRepository, times(1)).saveWithLockingPessimistic(balance);
    }

    @Test
    void testDepositToAccount_BalanceNotFound() {
        String accountId = "123";
        when(balanceRepository.findById(accountId)).thenReturn(Optional.empty());

        Optional<Balance> result = balanceService.depositToAccount(accountId, BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(75));

        assertTrue(result.isEmpty(), "Expected no balance found for the given account ID");
        verify(balanceRepository, times(1)).findById(accountId);
        verify(balanceRepository, never()).saveWithLockingPessimistic(any(Balance.class));
    }

    @Test
    void testGetAllBalances() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Balance> balanceList = List.of(new Balance(), new Balance());
        Page<Balance> page = new PageImpl<>(balanceList);
        when(balanceRepository.findAll(pageable)).thenReturn(page);

        Page<Balance> result = balanceService.getAllBalances(pageable);

        assertEquals(page, result, "Expected the page of balances to match the repository result");
        verify(balanceRepository, times(1)).findAll(pageable);
    }
}