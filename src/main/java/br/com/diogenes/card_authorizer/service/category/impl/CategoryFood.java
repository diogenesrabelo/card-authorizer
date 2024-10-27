package br.com.diogenes.card_authorizer.service.category.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.category.Category;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CategoryFood implements Category {

    private final BalanceRepository balanceRepository;

    public CategoryFood(final BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }
    @Override
    public TransactionResponse processTransaction(Balance balance, BigDecimal amount) {
        if (balance.getFood().compareTo(amount) >= 0) {
            balance.setFood(balance.getFood().subtract(amount));
            balanceRepository.saveWithLockingPessimistic(balance);
            return new TransactionResponse("00");
        } else {
            var cash = new CategoryCash(balanceRepository);
            return cash.processTransaction(balance, amount);
        }
    }
}
