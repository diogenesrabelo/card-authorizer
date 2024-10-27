package br.com.diogenes.card_authorizer.service.category;

import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;

import java.math.BigDecimal;

public interface Category {

    TransactionResponse processTransaction(Balance balance, BigDecimal amount);
}
