package br.com.diogenes.card_authorizer.service.authorizer.impl;

import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.authorizer.AuthorizerService;
import br.com.diogenes.card_authorizer.service.balance.BalanceService;
import br.com.diogenes.card_authorizer.service.category.Category;
import br.com.diogenes.card_authorizer.service.category.factory.CategoryFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthorizerServiceImpl implements AuthorizerService {

    private final BalanceService balanceService;
    private final CategoryFactory categoryFactory;

    public AuthorizerServiceImpl(final BalanceService balanceService, final CategoryFactory categoryFactory) {
        this.balanceService = balanceService;
        this.categoryFactory = categoryFactory;
    }

    @Override
    public TransactionResponse authorizer(Transaction transaction) {
        try {
            Balance balance = balanceService.findBalance(transaction.account()).orElseThrow();
            BigDecimal amount = transaction.totalAmount();
            String mcc = transaction.mcc();

            Category category = categoryFactory.getCategoryFromMCC(mcc, transaction.merchant());

            return category.processTransaction(balance, amount);

        } catch (Exception ex) {
            return new TransactionResponse("07");
        }
    }
}
