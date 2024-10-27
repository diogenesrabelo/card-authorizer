package br.com.diogenes.card_authorizer.service.category.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.category.Category;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CategoryCash implements Category {

    private final BalanceRepository balanceRepository;

    public CategoryCash(final BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public TransactionResponse processTransaction(Balance balance, BigDecimal amount) {
        if (balance.getCash().compareTo(amount) >= 0) {
            balance.setCash(balance.getCash().subtract(amount));
            balanceRepository.saveWithLockingPessimistic(balance);
            return new TransactionResponse("00");
        } else {
            return new TransactionResponse("51");
        }
    }
}
