package br.com.diogenes.card_authorizer.repository.balance.impl;

import br.com.diogenes.card_authorizer.repository.balance.CustomBalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public class CustomBalanceRepositoryImpl implements CustomBalanceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public <T extends Balance> T saveWithLockingPessimistic(T balance) {

        if (balance.getAccount() == null) {
            entityManager.persist(balance);
        } else {
            entityManager.merge(balance);
        }

        return balance;
    }
}
