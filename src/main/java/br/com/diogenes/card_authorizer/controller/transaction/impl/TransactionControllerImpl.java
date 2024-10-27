package br.com.diogenes.card_authorizer.controller.transaction.impl;

import br.com.diogenes.card_authorizer.controller.transaction.TransactionController;
import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import br.com.diogenes.card_authorizer.service.authorizer.AuthorizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionControllerImpl implements TransactionController {

    private final AuthorizerService authorizerService;

    public TransactionControllerImpl(AuthorizerService authorizerService) {
        this.authorizerService = authorizerService;
    }

    @Override
    public ResponseEntity<TransactionResponse> authorizeTransaction(Transaction transaction) {
        return ResponseEntity.ok(authorizerService.authorizer(transaction));
    }
}
