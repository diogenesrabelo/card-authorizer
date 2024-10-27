package br.com.diogenes.card_authorizer.service.authorizer;

import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;

public interface AuthorizerService {

    TransactionResponse authorizer(Transaction transaction);
}
