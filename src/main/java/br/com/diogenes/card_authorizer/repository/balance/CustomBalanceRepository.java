package br.com.diogenes.card_authorizer.repository.balance;

import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;

public interface CustomBalanceRepository {
    <T extends Balance> T saveWithLockingPessimistic(T balance);
}
